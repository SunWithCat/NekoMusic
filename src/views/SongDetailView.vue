<script setup>
import { usePlayerStore } from '@/stores/player.js';
import { computed, watch, ref } from 'vue';
import { playlists } from '../data/home.js'
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const playerStore = usePlayerStore();

const currentLineIndex = ref(0);
const lyricsContainerRef = ref(null);
const lyricLineRefs = ref([]);

const currentSong = computed(() => {
    // 增加安全检查！
    if (!route.params || !route.params.id) {
        return null; // 如果路由参数还没准备好，就先返回 null
    }

    const songId = Number(route.params.id);
    for (const playlist of playlists) {
        const foundSong = playlist.songs.find(song => song.id === songId);
        if (foundSong) {
            return foundSong;
        }
    }
    return null;
});

const parsedLyrics = computed(() => {
    if (!currentSong.value || !currentSong.value.lyrics) {
        return [{ time: 0, text: '暂无歌词' }];
    }
    const lines = currentSong.value.lyrics.trim().split('\n');
    const result = [];
    for (const line of lines) {
        const match = line.match(/\[(\d{2}):(\d{2})\.(\d{2,3})\](.*)/);
        if (match) {
            const minutes = parseInt(match[1]);
            const seconds = parseInt(match[2]);
            const milliseconds = parseInt(match[3].padEnd(3, '0'));
            const time = minutes * 60 + seconds + milliseconds / 1000;
            const text = match[4].trim();
            result.push({ time, text });
        }
    }
    if (result.length == 0) {
        return [{ time: 0, text: '歌词格式解析失败' }];
    }

    return result;
});

watch(() => playerStore.currentTime, (newTime) => {
    if (!parsedLyrics.value || parsedLyrics.value.length <= 1) return;
    for (let i = parsedLyrics.value.length - 1; i >= 0; i--) {
        if (parsedLyrics.value[i].time <= newTime) {
            currentLineIndex.value = i;
            return;
        }
    }
});

watch(currentLineIndex, (newIndex) => {
    if (lyricLineRefs.value[newIndex] && lyricsContainerRef.value) {
        const lineEl = lyricLineRefs.value[newIndex];
        lineEl.scrollIntoView({
            behavior: 'smooth',
            block: 'center',
        });
    }
});

const goBack = () => {
    console.log('Inspecting the router object:', router);
    router.back();
};

</script>

<template>
    <div class="song-detail-container">
        <button @click="goBack" class="back-button">返回</button>
        <div v-if="currentSong" class="song-content">
            <div class="cover-section">
                <img :src="currentSong.coverImgUrl" :alt="currentSong.name" class="song-cover">
                <h2>{{ currentSong.name }}</h2>
                <p>{{ currentSong.artist }}</p>
            </div>
            <div class="lyrics-section">
                <div class="lyrics-scroll-container" ref="lyricsContainerRef">
                    <p v-for="(line, index) in parsedLyrics" :key="index" class="lyric-line"
                        :class="{ active: index === currentLineIndex }"
                        :ref="el => { if (el) lyricLineRefs[index] = el }">
                        {{ line.text }}
                    </p>
                </div>
            </div>
        </div>
        <div v-else class="not-found">
            <p>歌曲信息加载失败 T_T</p>
        </div>
    </div>
</template>

<style scoped>
.song-detail-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40px;
    height: 100%;
    background-color: #f5f5f5;
}

.back-button {
    position: absolute;
    top: 80px;
    left: 40px;
    background: #e0e0e0;
    border: none;
    padding: 8px 16px;
    border-radius: 16px;
    cursor: pointer;
    font-size: 14px;
}

.song-content {
    display: flex;
    gap: 50px;
    width: 100%;
    max-width: 800px;
    margin-top: 50px;
}

.cover-section {
    flex: 1;
    text-align: center;
}

.song-cover {
    width: 250px;
    height: 250px;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.cover-section h2 {
    margin-top: 20px;
    font-size: 24px;
    font-weight: bold;
}

.cover-section p {
    margin-top: 10px;
    color: #666;
}

.lyrics-section {
    flex: 1;
}

.lyrics-scroll-container {
    height: 400px;
    overflow-y: auto;
    padding-right: 15px;
    text-align: center;
    padding: 0 20px;
    scrollbar-width: thin;
    scrollbar-color: #ccc #f5f5f5;
}

.lyrics-scroll-container::-webkit-scrollbar-track {
    background: transparent;
    /* 轨道背景设为透明，看起来更简洁 */
    border-radius: 3px;
}

.lyrics-scroll-container::-webkit-scrollbar-thumb {
    background-color: #ccc;
    /* 滑块颜色 */
    border-radius: 3px;
    transition: background-color .3s;
}

.lyrics-scroll-container::-webkit-scrollbar-thumb:hover {
    background-color: #bbb;
    /* 悬浮时颜色加深 */
}

.lyric-line {
    margin-bottom: 16px;
    font-size: 16px;
    color: #444;
    transition: color 0.3s, font-size 0.3s;
}

.lyric-line.active {
    font-weight: bold;
    font-size: 18px;
    transform: scale(1.05);
}

.not-found {
    text-align: center;
    margin-top: 100px;
    color: #888;
}
</style>