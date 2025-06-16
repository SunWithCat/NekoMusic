<script setup>
import { useRouter } from 'vue-router';
import { usePlayerStore } from '@/stores/player.js';
import SongCard from '../components/SongCard.vue'
import { banners, playlists } from '../data/home.js'

const router = useRouter();
const playerStore = usePlayerStore();

const handleCardClick = (clickedPlaylist) => {
    router.push({
        name: 'playlist-detail',
        params: {
            id: clickedPlaylist.id
        }
    });
};

// console.log('Banners:', banners);
// console.log('Playlists:', playlists);
</script>

<template>
    <div class="home-container">
        <div class="content">
            <el-carousel height="200px" motion-blur>
                <el-carousel-item v-for="banner in banners" :key="banner.id">
                    <img :src="banner.pic" class="banner-img" alt="banner">
                </el-carousel-item>
            </el-carousel>
            <div class="playlists">
                <h3 class="section-title">推荐歌单</h3>
                <div class="playlists-grid">
                    <SongCard v-for="playlist in playlists" :key="playlist.id" v-bind="playlist"
                        @click-card="handleCardClick" />
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.home-container {
    display: flex;
    justify-content: center;
    background-color: #f5f5f5;
    min-height: 100vh;
}

.content {
    width: 100%;
    max-width: 1200px;
    padding: 20px;
}

.banner-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 8px;
}

.section-title {
    font-size: 24px;
    font-weight: bold;
    margin-top: 30px;
    margin-bottom: 20px;
}

.playlists-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 20px;
}
</style>
