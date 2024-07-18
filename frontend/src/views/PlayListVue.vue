<template>
    <div style="background-color: black;">
        <div style="display: flex; flex-direction: column; align-items: center;">
            <h1 style="color: white;" >{{ title }}</h1>
            <template v-for="(item, index) in state.items" :key="index">
                <button style="width: 90%; margin: 1%;" @click="jumpTo('audioplayer')">
                    <RecordFile :item="item"></RecordFile>
                </button>
            </template>
        </div>
    </div>
</template>

<script setup>
import RecordFile from '@/components/RecordFile.vue';
import { ref } from 'vue';
import { reactive } from 'vue';
import { useRouter } from "vue-router";

const router = useRouter();

const title = ref('주워 듣기(실시간 알람함)');

const state = reactive({
    title: '재생목록',
    items: [
        {
            title: '부들부들 솜사탕 썰 푼다',
            imgUrl: 'https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png',
            recordId: 123,
        },
        {
            title: '부장님이랑 원펀치 뜬 썰 푼다',
            imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXobLcA4-7fiJkSmqBaQnd2QL8gY8EFIx4pQ&s',
            recordId: 123,
        },
        {
            title: '갈매미가 내 돈 뺏어감',
            imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgnrFfdTo9x-dksemfsNqIVUytfkP6mKDUiODsYF8Q&s',
            recordId: 123,
        },
        {
            title: '부들부들 솜사탕 썰 푼다',
            imgUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRE3Sghlf91X3SuEznW--hL06WkQRzOC3yBKg&s',
            recordId: 123,
        },
        {
            title: '부장님이랑 원펀치 뜬 썰 푼다',
            imgUrl: 'https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png',
            recordId: 123,
        },
        {
            title: '갈매미가 내 돈 뺏어감',
            imgUrl: 'https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png',
            recordId: 123,
        },
        {
            title: '갈매미가 내 돈 뺏어감',
            imgUrl: 'https://png.pngtree.com/thumb_back/fw800/background/20231008/pngtree-d-render-of-a-golden-tooth-exploring-dental-and-medical-concepts-image_13574408.png',
            recordId: 123,
        },
    ]

});

function playRecord(id) {
  alert(`Image clicked: `+id);
}

function jumpTo(routeName) {
    router.push({ name: routeName });
}

</script>

<style lang="scss" scoped></style>