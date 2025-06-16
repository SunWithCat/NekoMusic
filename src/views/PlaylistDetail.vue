<script setup>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import { playlists } from '../data/home.js';
import { usePlayerStore } from '@/stores/player.js';


const route = useRoute();
const playerStore = usePlayerStore();

// const playlistId = route.params.id;

const currentPlaylist = computed(() => {
    const playlistId = route.params.id;
    console.log(`正在查找 ID: ${playlistId} (类型: ${typeof playlistId})`);
    return playlists.find(p => {
        return p.id === Number(playlistId);
    });
});

const playSong = (song, index) => {
    if (!currentPlaylist.value) return;
    playerStore.setPlaylist(currentPlaylist.value.songs, index);
};

const formatDuration = (seconds) => {
    if (isNaN(seconds) || seconds < 0) return '00:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    const formattedMins = String(mins).padStart(2, '0');
    const formattedSecs = String(secs).padStart(2, '0');

    return `${formattedMins}:${formattedSecs}`;
}

</script>

<template>
    <div class="playlist-detail-container">
        <div v-if="currentPlaylist" class="content">
            <img :src="currentPlaylist.coverImgUrl" alt="cover" class="cover-image">
            <div class="details-wrapper">
                <div class="info">
                    <h1>{{ currentPlaylist.name }}</h1>
                    <p>播放量：{{ currentPlaylist.playCount }}</p>
                    <router-link to="/">返回首页</router-link>
                </div>
                <div class="song-list">
                    <div class="song-item header">
                        <span class="index">#</span>
                        <span class="title">标题</span>
                        <span class="artist">歌手</span>
                        <span class="duration">时长</span>
                    </div>
                    <div class="song-item" v-for="(song, index) in currentPlaylist.songs" :key="song.id"
                        @click="playSong(song, index)">
                        <span class="index">{{ index + 1 }}</span>
                        <span class="title">{{ song.name }}</span>
                        <span class="artist">{{ song.artist }}</span>
                        <span class="duration">{{ formatDuration(song.duration) }}</span>
                    </div>
                </div>
            </div>
        </div>

        <div v-else class="not-found">
            <p>没有找到这个歌单哦。</p>
            <router-link to="/">返回首页</router-link>
        </div>
    </div>
</template>

<style scoped>
.playlist-detail-container {
    padding: 40px;
    display: flex;
    justify-content: center;
}

.content {
    display: flex;
    gap: 30px;
    max-height: calc(100vh - 160px);
}

.cover-image {
    width: 200px;
    height: 200px;
    border-radius: 8px;
    flex-shrink: 0;
}

.details-wrapper {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    min-width: 0;
}

.back-link {
    display: inline-block;
    margin-top: 20px;
    background-color: #eee;
    padding: 8px 16px;
    border-radius: 16px;
    text-decoration: none;
    color: #333;
}

.info {
    margin-bottom: 20px;
    flex-shrink: 0;
}

.info h1 {
    font-size: 24px;
    font-weight: bold;
}

.info p {
    margin-top: 10px;
    color: #666;
}

.info a {
    display: inline-block;
    margin-top: 20px;
    background-color: #eee;
    padding: 8px 16px;
    border-radius: 16px;
    text-decoration: none;
    color: #333
}

.song-list {
    flex-grow: 1;
    overflow-y: auto;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
}

.song-item {
    display: grid;
    grid-template-columns: 50px 1fr 200px 100px;
    align-items: center;
    padding: 12px 20px;
    border-bottom: 1px solid #f2f2f2;
    transition: background-color 0.2s;
    font-size: 14px;
    column-gap: 15px;
}

.song-item:last-child {
    border-bottom: none;
}

.song-item:not(.header):hover {
    background-color: #f0f0f0;
    cursor: pointer;
}

.song-item.header {
    color: #888;
    font-size: 12px;
}

.index {
    color: #ccc;
}

.title {
    font-weight: 500;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.artist {
    color: #666;
}

.not-found {
    text-align: center;
    padding-top: 50px;
}

.duration {
    color: #999;
    text-align: right;
    /* 让时长靠右对齐，更美观 */
}
</style>