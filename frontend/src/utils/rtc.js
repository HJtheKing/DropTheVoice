export function openDB() {
    return new Promise((resolve, reject) => {
        const idxedDB = window.indexedDB;
        if (!idxedDB) {
            window.alert('indexedDB를 지원하지 않는 브라우저');
        } else {
            const request = idxedDB.open('dropthevoice', 1);  // 데이터베이스 열기

            request.onupgradeneeded = (e) => { 
                const db = e.target.result;
                if (!db.objectStoreNames.contains('rtc_voice')) {
                    db.createObjectStore('rtc_voice', { keyPath: 'id' });  // 객체 저장소 생성
                }
            };

            request.onsuccess = (e) => {
                console.log('Database opened successfully');
                resolve(e.target.result);  // 데이터베이스 연결을 resolve로 반환
            };

            request.onerror = (e) => {
                console.log('Database opening failed');
                reject("Error : " + e.target.errorCode);  // 에러 발생 시 reject
            };
        }
    });
}

export function saveVoice(key, value) {
    return openDB().then((db) => {
        return new Promise((resolve, reject) => {
            const transaction = db.transaction(['rtc_voice'], 'readwrite');
            const store = transaction.objectStore('rtc_voice');

            const request = store.put({ id: key, value });  // id 키 사용
            request.onsuccess = () => resolve();  // 저장 성공 시 resolve
            request.onerror = (e) => reject("Error while saving: " + e.target.errorCode);  // 저장 실패 시 reject
        });
    });
}

export function getVoice(key) {
    return openDB().then((db) => {
        return new Promise((resolve, reject) => {
            const transaction = db.transaction(['rtc_voice'], 'readonly');
            const store = transaction.objectStore('rtc_voice');

            const request = store.get(key);  // key를 이용해 데이터 가져오기
            request.onsuccess = (e) => {
                resolve(e.target.result?.value);  // 성공 시 value 반환
            }
            request.onerror = (e) => reject("Error while getting: " + e.target.errorCode);  // 실패 시 reject
        });
    });
}

// export function getVoice(key) {
//     return openDB().then((db) => {
//         const transaction = db.transaction(['rtc_voice'], 'readonly');
//         const store = transaction.objectStore('rtc_voice');
//         const request = store.get(key);  // key를 이용해 데이터 가져오기
//         request.onsuccess = (e) => {
//             return e.target.result?.value; // 성공 시 value 반환
//         }
//         request.onerror = (e) => {
//             console.log("Error while getting: " + e.target.errorCode);  // 실패 시 reject
//             return;
//         }
//     });
// }

export function deleteDB(key){
    return new Promise((resolve, reject) => {
        const request = indexedDB.deleteDatabase(key);
        request.onsuccess = (e) => {
            console.log(`indexedDB ${key} deleted`);
            resolve();
        }
        request.onerror = (e) => {
            console.log(`Error occured deleting indexedDB ${key}`);
            reject(e.target.errorCode);
        }
        request.onblocked = () => {
            console.warn(`Deleting indexedDB ${key} is blocked`)
            reject('indexedDB deletion blocked')
        }
    })
}
