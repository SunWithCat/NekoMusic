<script setup>
import { usePlayerStore } from '@/stores/player';
import { ref, watch, computed, nextTick } from 'vue';
import IconNext from './icons/IconNext.vue';
import IconPause from './icons/IconPause.vue';
import IconPlay from './icons/IconPlay.vue';
import IconPrev from './icons/IconPrev.vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';

const playerStore = usePlayerStore();
const audioRef = ref(null);

const isSeeking = ref(false);

const router = useRouter(); // 初始化 router 用于跳转
const route = useRoute();   // 初始化 route 用于获取当前路由信息

const audioSrc = computed(() => {
    if (playerStore.currentSong) {
        return `http://localhost:8080/api/music/${playerStore.currentSong.id}`
    }
    return null;
})

// 新增一个计算属性，判断当前是否就在这首歌的详情页
const isAlreadyOnDetailPage = computed(() => {
    if (!playerStore.currentSong) return false;
    // 如果当前路由名是 'song-detail' 且 路由的id和当前播放歌曲的id一致
    return route.name === 'song-detail' && Number(route.params.id) === playerStore.currentSong.id;
});

// 新增点击事件处理函数
const goToSongDetail = () => {
    // 如果没有当前歌曲，或者已经在详情页，则不执行任何操作
    if (!playerStore.currentSong || isAlreadyOnDetailPage.value) {
        return;
    }
    // 否则，跳转到歌曲详情页
    router.push({
        name: 'song-detail',
        params: { id: playerStore.currentSong.id }
    });
};

watch(audioSrc, (newSrc) => {
    if (newSrc && audioRef.value) {
        nextTick(() => {
            audioRef.value.load();
            if (playerStore.isPlaying) {
                audioRef.value.play().catch((e) => console.error('自动播放失败：', e))
            }
        })
    }
})

const formatDuration = (seconds) => {
    if (isNaN(seconds) || seconds < 0) return '00:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    const formattedMins = String(mins).padStart(2, '0');
    const formattedSecs = String(secs).padStart(2, '0');
    return `${formattedMins}:${formattedSecs}`;
};

const handleTimeUpdate = () => {
    if (isSeeking.value) return; // 取消更新时间
    if (audioRef.value) {
        playerStore.updateCurrentTime(audioRef.value.currentTime);
    }
};

const handleLoadedMetadata = () => {
    if (audioRef.value) {
        const duration = audioRef.value.duration;
        // 更严格的duration验证
        if (Number.isFinite(duration) && duration > 0) {
            console.log("有效音频时长:", duration);
            playerStore.updateDuration(duration);
        } else {
            console.warn("获取到的音频时长无效:", duration, "检查音频源是否有效:", audioRef.value.src);
            playerStore.updateDuration(0);
            audioRef.value.load();
        }
    }
};

const handleAudioError = (e) => {
    console.error("音频加载错误:", e);
    playerStore.isPlaying = false;
    if (audioRef.value) {
        console.warn("当前音频源:", audioRef.value.src);
        audioRef.value.load(); // 尝试重新加载
    }
};

const handleSeek = (event) => {
    if (!audioRef.value) return;
    const newTime = Number(event.target.value);
    audioRef.value.currentTime = newTime;
    playerStore.seek(newTime);
};

const endSeek = () => {
    isSeeking.value = false;
    if (playerStore.isPlaying) {
        audioRef.value?.play();
    }
};

const handleTogglePlay = () => {
    if (!audioRef.value || !playerStore.currentSong) return;

    if (playerStore.isPlaying) {
        audioRef.value.pause();
    } else {
        // 尝试播放，如果失败则捕获错误
        audioRef.value.play().catch(err => {
            console.warn("播放失败，可能需要用户交互。", err);
            playerStore.isPlaying = false; // 播放失败时重置状态
        });
        playerStore.isPlaying = !playerStore.isPlaying;
    }
};

const progressPercentage = computed(() => {
    if (playerStore.duration > 0) {
        return (playerStore.currentTime / playerStore.duration) * 100;
    }
    return 0;
})

</script>

<template>
    <div class="player-bar-container">
        <input type="range" class="progress-bar-top" :max="playerStore.duration > 0 ? playerStore.duration : 0"
            :value="playerStore.currentTime" :disabled="playerStore.duration <= 0" @input="handleSeek"
            @mousedown="isSeeking = true" @mouseup="endSeek"
            :style="{ '--progress-percent': `${progressPercentage}%` }">
        <div v-if="playerStore.currentSong" class="song-info" :class="{ clickable: !isAlreadyOnDetailPage }"
            @click="goToSongDetail">
            <img :src="playerStore.currentSong.coverImgUrl" alt="cover" class="song-cover">
            <div class="song-details">
                <p class="song-name">{{ playerStore.currentSong.name }}</p>
                <p class="song-artist">{{ playerStore.currentSong.artist }}</p>
            </div>
        </div>
        <div v-else class="song-info">
            <p>欢迎使用</p>
        </div>

        <div class="player-controls-wrapper">
            <div class="controls">
                <button class="control-btn" @click="playerStore.playPrev()">
                    <IconPrev />
                </button>
                <button class="control-btn play-pause-btn" @click="handleTogglePlay()">
                    <IconPause v-if="playerStore.isPlaying" />
                    <IconPlay v-else />
                </button>
                <button class="control-btn" @click="playerStore.playNext()">
                    <IconNext />
                </button>
            </div>
        </div>
        <div class="other-controls">
            <div class="time-display">
                {{ formatDuration(playerStore.currentTime) }} / {{ formatDuration(playerStore.duration) }}
            </div>
        </div>
    </div>
    <audio ref="audioRef" :src="audioSrc" @ended="playerStore.playNext()" @timeupdate="handleTimeUpdate"
        @loadedmetadata="handleLoadedMetadata" @error="handleAudioError" @play="playerStore.isPlaying = true"
        @pause="playerStore.isPlaying = false"></audio>
</template>

<style scoped>
.player-bar-container {
    position: relative;
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
    justify-self: start;
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
    margin-top: 4px;
}

.song-info.clickable {
    cursor: pointer;
    transition: background-color 0.2s;
}

.other-controls {
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

.play-pause-btn {
    width: 40px;
    height: 40px;
    color: #ec4141;
    transition: transform 0.2s;
}

.play-pause-btn:hover {
    transform: scale(1.1);
}

.player-controls-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 5px;
    width: 70%;
}

.controls {
    justify-self: center;
    display: flex;
    align-items: center;
    gap: 20px;
}

.progress-bar-top {
    position: absolute;
    top: -5px;
    left: 0;
    width: 100%;
    height: 6px;
    margin: 0;
    -webkit-appearance: none;
    appearance: none;
    background-color: transparent;
    cursor: pointer;
}

.progress-bar-top::-webkit-slider-runnable-track {
    height: 4px;
    border-radius: 2px;
    background: linear-gradient(to right,
            #ec4141 var(--progress-percent),
            #e0e0e0 var(--progress-percent));
}

.progress-bar-top:disabled::-webkit-slider-runnable-track {
    background: #f0f0f0;
    cursor: not-allowed;
}

.progress-bar-top:disabled::-webkit-slider-thumb {
    background: #cccccc;
    cursor: not-allowed;
}

.progress-bar-top::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 12px;
    height: 12px;
    border-radius: 50%;
    background: #ec4141;
    margin-top: -5px;
}

.main-controls {
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    align-items: center;
    height: 100%;
    padding: 0 20px;
}

.time-display {
    color: #666;
    font-size: 12px;
    min-width: 100px;
    /* 给一个最小宽度防止抖动 */
    text-align: center;
}
</style>
