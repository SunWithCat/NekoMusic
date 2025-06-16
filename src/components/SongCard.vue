<script setup>

const props = defineProps({
    coverImgUrl: {
        type: String,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    playCount: {
        type: Number,
        required: true
    },
    id: {
        type: Number,
        required: true
    }
});

const emit = defineEmits(['click-card']);

const formatPlayCount = (count) => {
    if (count > 100000000) {
        return `${(count / 100000000).toFixed(1)}亿`;
    }
    if (count > 10000) {
        return `${Math.floor(count / 10000)}万`;
    }
    return count;
}

function onCardClick(event) {
    event.stopPropagation();
    emit('click-card', props);
}

</script>

<template>
    <div class="playlist-card" @click="onCardClick">
        <img :src="coverImgUrl" alt="playlist-cover" class="playlist-cover">
        <p class="playlist-name">{{ name }}</p>
        <p class="playlist-playcount">播放量：{{ formatPlayCount(playCount) }}</p>
    </div>
</template>

<style scoped>
.playlist-card {
    cursor: pointer;
    transition: transform 0.3s ease;
}

.playlist-card:hover {
    transform: translateY(-5px);
}

.playlist-cover {
    width: 100%;
    border-radius: 8px;
    aspect-ratio: 1 / 1;
}

.playlist-name {
    font-size: 14px;
    margin-top: 8px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    line-clamp: 2;
    -webkit-box-orient: vertical;
    color: black;
}

.playlist-playcount {
    font-size: 12px;
    color: black;
    margin-top: 4px;
}
</style>