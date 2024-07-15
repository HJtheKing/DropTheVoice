<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    item: Object
});
</script>

<template>
    <div style="background-color: lightgray; border-radius: 5px; height: 80px; display: flex;">
        <div style="min-width: 95%; max-width: 95%; display: flex; flex-direction: row; align-items: center;">
            <div style="min-width: 40%; max-width: 40%;">
                <img :src="props.item.imgUrl" style="border: 2px solid gray; padding: 2%;  min-width: 90%; max-width: 90%;  height:70px; border-radius: 5%;" />
            </div>
            {{ item.title }}
        </div>
    </div>
</template>

<style lang="scss" scoped></style>