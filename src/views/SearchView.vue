<script setup>
import { ref, computed, watch } from 'vue';
import axios from 'axios';
import SongCard from '../components/SongCard.vue'
import { useRouter } from 'vue-router';
import { usePlayerStore } from '@/stores/player.js';

const router = useRouter();
const playerStore = usePlayerStore();

const searchQuery = ref('');
const foundPlaylists = ref([]);
const foundSongs = ref([]);
const isLoading = ref(false);


const debounce = (func, delay) => {
    let debounceTimer;
    return (...args) => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            func(...args);
        }, delay);
    };
};

const performSearch = async () => {
    if (!searchQuery.value.trim()) {
        foundPlaylists.value = [];
        foundSongs.value = [];
        return;
    }
    isLoading.value = true;
    try {
        const response = await axios.get(`http://localhost:8080/api/search`, {
            params: { query: searchQuery.value }
        });
        foundPlaylists.value = response.data.playlists;
        foundSongs.value = response.data.songs;
    } catch (error) {
        console.error("搜索失败:", error);
    } finally {
        isLoading.value = false;
    }
};

watch(searchQuery, debounce(performSearch, 300));

const handlePlaylistClick = (playlist) => {
    router.push({ name: 'playlist-detail', params: { id: playlist.id } });
};

const playSong = (song) => {
    if (song && typeof song.id === 'number') {
        const songToPlay = {
            id: song.id,
            name: song.name,
            artist: song.artist,
            coverImgUrl: song.coverImgUrl,
            duration: song.duration,
        };
        playerStore.setPlaylist([songToPlay], 0);
    } else {
        console.error("尝试播放一个无效的歌曲对象: ", song);
    }
}

const formatDuration = (seconds) => {
    if (isNaN(seconds) || seconds < 0) return '00:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
};

</script>

<template>
    <div class="search-container">
        <div class="search-bar">
            <input type="text" v-model="searchQuery" placeholder="搜索歌单或歌曲" class="search-input">
        </div>
        <div class="results-container">
            <div v-if="isLoading" class="loading-state">
                正在努力搜索中...
            </div>
            <div v-else-if="!searchQuery.trim()" class="empty-state">
                试着搜索点什么吧
            </div>
            <div v-else-if="foundPlaylists.length == 0 && foundSongs.length == 0" class="no-results">
                <p>未能找到与“{{ searchQuery }}”相关的任何内容</p>
            </div>
            <div v-else class="search-results">
                <section v-if="foundPlaylists.length > 0">
                    <h3 class="section-title">相关歌单</h3>
                    <div class="playlists-grid">
                        <SongCard v-for="playlist in foundPlaylists" :key="playlist.id" v-bind="playlist"
                            @click-card="handlePlaylistClick(playlist)" />
                    </div>
                </section>
                <section v-if="foundSongs.length > 0">
                    <h3 class="section-title">相关单曲</h3>
                    <div class="song-list">
                        <div class="song-item" v-for="song in foundSongs" :key="song.id" @click="playSong(song)">
                            <img :src="song.coverImgUrl" alt="cover" class="song-cover">
                            <div class="song-info">
                                <div class="song-name">{{ song.name }}</div>
                                <div class="song-artist">{{ song.artist }}</div>
                            </div>
                            <span class="song-duration">{{ formatDuration(song.duration) }}</span>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>
</template>

<style scoped>
.search-container {
    padding: 30px 40px;
}

.search-bar {
    display: flex;
    justify-content: center;
    margin-bottom: 40px;
}

.search-input {
    width: 50%;
    max-width: 400px;
    padding: 12px 20px;
    font-size: 16px;
    border-radius: 20px;
    border: 1px solid #ddd;
    outline: none;
    transition: border-color 0.2s, box-shadow 0.2s;
}

.search-input:focus {
    border-color: #ec4141;
    box-shadow: 0 0 5px rgba(236, 65, 65, 0.5);
}

.search-results {
    margin-top: 20px;
    padding: 12px;
}

.loading-state,
.empty-state,
.no-results {
    text-align: center;
    color: #888;
    margin-top: 10px;
    font-size: 16px;
}

.section-title {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 15px;
    margin-top: 10px;
    border-left: 4px solid #ec4141;
    padding-left: 10px;
}

.playlists-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    gap: 20px;
}

.song-list {
    display: flex;
    flex-direction: column;
}

.song-item {
    display: flex;
    align-items: center;
    padding: 10px;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.2s;
}

.song-item:hover {
    background-color: #f0f0f0;
}

.song-cover {
    width: 50px;
    height: 50px;
    border-radius: 4px;
    margin-right: 15px;
}

.song-info {
    flex-grow: 1;
}

.song-name {
    font-size: 15px;
    color: #333;
}

.song-artist {
    font-size: 13px;
    color: #666;
    margin-top: 5px;
}

.song-duartion {
    font-size: 13px;
    color: #999;
    margin-left: 20px;
}
</style>