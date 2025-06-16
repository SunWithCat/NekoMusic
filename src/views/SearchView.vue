<style scoped>
.search-container {
    padding: 40px;
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

.no-results {
    text-align: center;
    color: #666;
}

.playlists-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 20px;
}
</style>

<template>
    <div class="search-container">
        <div class="search-bar">
            <input type="text" v-model="searchQuery" placeholder="搜索歌单或歌曲" class="search-input">
        </div>
        <div class="results-container">
            <div v-if="searchQuery.trim() && filteredPlaylists.length === 0" class="no-results">
                <p>未能找到与“{{ searchQuery }}”的相关结果。</p>
            </div>
            <div v-else class="playlist-grid">
                <SongCard v-for="playlist in filteredPlaylists" :key="playlist.id" v-bind="playlist" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { playlists } from '../data/home.js';
import SongCard from '../components/SongCard.vue'

const searchQuery = ref('');
const filteredPlaylists = computed(() => {
    if (!searchQuery.value.trim()) {
        return [];
    }
    return playlists.filter(playlist =>
        playlist.name.toLowerCase().includes(searchQuery.value.toLocaleLowerCase())
    );
})

</script>
