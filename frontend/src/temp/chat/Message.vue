<script setup>
import { onMounted, ref } from 'vue';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
const props = defineProps(["message"]);
const messageContainerClass = ref("message-container");
const isMe = ref(false);
dayjs.extend(relativeTime);
onMounted(() => {
    isMe.value = props.message.isMe;
    if (isMe.value) {
        messageContainerClass.value = "message-container-me";
    }
})
</script>

<template>
    <a-comment>
        <template #author><a>{{message.writer}}</a></template>
        <template #content>
            <p style="margin-left: 3px;">
                {{message.content}}
            </p>
        </template>
        <template #datetime>
            <a-tooltip :title="dayjs().format('YYYY-MM-DD HH:mm:ss')">
                <span>{{ dayjs(message.time).format("YYYY-MM-DD HH:mm:ss")}}</span>
            </a-tooltip>
        </template>
    </a-comment>
</template>

<style scoped>

</style>