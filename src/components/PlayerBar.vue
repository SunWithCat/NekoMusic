<script setup>
import { usePlayerStore } from '@/stores/player';
import { ref, watch } from 'vue';
import IconNext from './icons/IconNext.vue';
import IconPause from './icons/IconPause.vue';
import IconPlay from './icons/IconPlay.vue';
import IconPrev from './icons/IconPrev.vue';

const playerStore = usePlayerStore();

const audioRef = ref(null);

watch(() => playerStore.isPlaying, (newIsPlaying) => {
    if (!audioRef.value) return; // 确保audio元素已加载

    if (newIsPlaying) {
        audioRef.value.play(); // 调用play方法
    } else {
        audioRef.value.pause(); // 调用pause方法
    }

});

watch(() => playerStore.currentSong, (newSong) => {
    if (!audioRef.value) return;

    if (newSong) {
        // 歌曲变化时，自动播放
        audioRef.value.play().catch(err => {
            console.warn("自动播放失败，等待用户手动播放", err);
            playerStore.isPlaying = false;
        });
    }
}, {
    deep: true
});

</script>

<template>
    <div class="player-bar-container">
        <div v-if="playerStore.currentSong" class="song-info">
            <img :src="playerStore.currentSong.coverImgUrl" alt="cover" class="song-cover">
            <div class="song-details">
                <p class="song-name">{{ playerStore.currentSong.name }}</p>
            </div>
        </div>
        <div v-else class="song-info">
            <p>欢迎使用</p>
        </div>

        <div class="controls">
            <button class="control-btn" @click="playerStore.playPrev()">
                <IconPrev />
            </button>
            <button class="control-btn play-pause-btn" @click="playerStore.togglePlayStatus()">
                <IconPause v-if="playerStore.isPlaying" />
                <IconPlay v-else />
            </button>
            <button class="control-btn" @click="playerStore.playNext()">
                <IconNext />
            </button>
        </div>
        <div class="other-controls">
            <p>其他功能</p>
        </div>
    </div>
    <audio ref="audioRef" :src="playerStore.currentSong?.url" @ended="playerStore.playNext()"></audio>
</template>

<style scoped>
.player-bar-container {
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    align-items: center;
    height: 100%;
    padding: 0 20px;
    background-color: #fff;
}

.song-info {
    grid-column: 1 / 2;
    display: flex;
    align-items: center;
    gap: 12px;
    overflow: hidden;
}

.song-cover {
    width: 48px;
    height: 48px;
    border-radius: 4px;
    flex-shrink: 0;
}

.song-details {
    overflow: hidden;
}

.song-name {
    font-size: 14px;
    font-weight: bold;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.song-artist {
    font-size: 12px;
    color: #666;
}

.other-controls {
    /* width: 300px; */
    /* text-align: right; */
    justify-self: end;
}

.controls {
    justify-self: center;
    display: flex;
    align-items: center;
    gap: 15px;
}

.control-btn {
    background: none;
    border: none;
    padding: 0;
    color: #333;
    width: 48px;
    height: 48px;
    cursor: pointer;
    transition: color 0.2s;
}

.control-btn:hover {
    color: #ec4141;
}

/* 中间的播放/暂停按钮可以更大更突出 */
.play-pause-btn {
    width: 40px;
    height: 40px;
    color: #ec4141;
    /* 主色调 */
    transition: transform 0.2s;
}

.play-pause-btn:hover {
    transform: scale(1.1);
    /* 鼠标悬浮时放大 */
    color: #ec4141;
    /* 保持红色 */
}
</style>