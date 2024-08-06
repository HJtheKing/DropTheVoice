import requests
from flask import Flask, request, jsonify
import boto3
import json
import os
import librosa
import soundfile as sf
from io import BytesIO
from urllib.parse import urlparse, unquote

app = Flask(__name__)

@app.route('/process-audio', methods=['POST'])
def process_audio():
    try:
        # Spring으로부터 JSON 객체를 전달받음
        data = request.get_json()

        voice = data['voice']
        aws_access_key = data['aws_access_key']
        aws_secret_key = data['aws_secret_key']
        aws_region = data['aws_region']
        aws_bucket = data['aws_bucket_name']
        pitch_shift = data['pitchShift']

        # S3 클라이언트 생성
        s3_client = boto3.client(
            's3',
            aws_access_key_id=aws_access_key,
            aws_secret_access_key=aws_secret_key,
            region_name=aws_region
        )

        # JSON 데이터 로그로 출력
        app.logger.info("Received JSON: %s", json.dumps(voice, indent=2))

        # S3 파일 경로 정보
        save_folder = voice['saveFolder']
        save_path = voice['savePath']

        # savePath에서 S3 키 추출 및 디코딩
        parsed_url = urlparse(save_path)
        s3_key = unquote(parsed_url.path.lstrip('/'))

        # 로그 출력: 다운로드할 S3 키
        app.logger.info(f"Downloading from S3 with key: {s3_key}")

        # S3에서 파일 다운로드
        response = s3_client.get_object(Bucket=aws_bucket, Key=s3_key)
        audio_bytes = response['Body'].read()

        # 메모리에서 파일 변조
        y, sr = librosa.load(BytesIO(audio_bytes), sr=None)
        y_shifted = librosa.effects.pitch_shift(y, sr=sr, n_steps=pitch_shift)

        # 변조된 파일을 메모리에서 저장
        output_buffer = BytesIO()
        sf.write(output_buffer, y_shifted, sr, format='mp3')
        output_buffer.seek(0)

        # 변조된 파일을 S3에 업로드
        output_filename = f"{os.path.splitext(os.path.basename(s3_key))[0]}_shifted{pitch_shift}.mp3"
        processed_s3_key = f"processed-voice/{output_filename}"  # 파일명을 URL 인코딩하지 않음

        # 로그 출력: 업로드할 S3 키
        app.logger.info(f"Uploading to S3 with key: {processed_s3_key}")

        # 콘텐츠 유형 설정
        s3_client.upload_fileobj(
            output_buffer,
            aws_bucket,
            processed_s3_key,
            ExtraArgs={'ContentType': 'audio/mpeg'}
        )

        # 변조된 파일 경로 반환
        processed_path = f"https://{aws_bucket}.s3.{aws_region}.amazonaws.com/{processed_s3_key}"

        response_json = {"processedPath": processed_path}
        return jsonify(response_json)
    except Exception as e:
        app.logger.error("Error processing audio: %s", str(e))
        return jsonify({"error": str(e)}), 500

# 0.0.0.0 으로 모든 IP에 대한 연결을 허용해놓고 포트는 5000으로 설정
if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)
