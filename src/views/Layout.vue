<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router';
import PlayerBar from '@/components/PlayerBar.vue';

import axios from 'axios';
import { onMounted, ref } from 'vue';

const originalLogoText = 'NekoMusic';
const backendMessage = ref('正在从后端加载数据...');
const showBackendMessage = ref(false);
const route = useRoute();

onMounted(async () => { // 生命周期钩子
    try {
        const response = await axios.get('http://localhost:8080/api/hello'); // 发送http请求，GET请求
        backendMessage.value = response.data; // 赋值
    } catch (error) {
        backendMessage.value = '加载失败 T_T: ' + error.message;
    }
});

const toggleLogoContent = () => {
    showBackendMessage.value = !showBackendMessage.value;
};
</script>

<template>
    <div class="layout-container">
        <header class="header">
            <div class="logo" @click="toggleLogoContent">
                <img src="../assets/logo.svg" class="svg-img">
                {{ showBackendMessage ? backendMessage : originalLogoText }}
            </div>
            <nav class="navigation">
                <RouterLink to="/" class="nav-link">发现音乐</RouterLink>
                <RouterLink to="/search" class="nav-link">搜索</RouterLink>
            </nav>
        </header>

        <main class="main-content">
            <RouterView v-slot="{ Component }">
                <transition name="fade" mode="out-in">
                    <component :is="Component" :key="route.path" />
                </transition>
            </RouterView>
        </main>

    </div>
    <div class="player-bar-fixed">
        <PlayerBar />
    </div>
</template>

<style scoped>
.layout-container {
    display: flex;
    flex-direction: column;
    height: 100vh;
    padding-bottom: 70px;
}

.header {
    height: 60px;
    background-color: #ec4141;
    color: white;
    display: flex;
    align-items: center;
    padding: 0 20px;
    flex-shrink: 0;
}

.logo {
    font-size: 20px;
    font-weight: bold;
    margin-right: 60px;
    cursor: pointer;
    display: flex;
    align-items: center;
}

.svg-img {
    width: 27px;
    height: 27px;
    padding-right: 3px;
}

.navigation {
    display: flex;
    gap: 20px;
}

.nav-link {
    color: #dcdcdc;
    text-decoration: none;
    font-size: 16px;
    padding: 8px 12px;
    border-radius: 4px;
    transition: background-color 0.2s, color 0.2s;
}

.nav-link:hover {
    background-color: rgba(255, 255, 255, 0.1);
    color: #fff;
}

.router-link-exact-active {
    background-color: rgba(255, 255, 255, 0.2);
    color: #fff;
    font-weight: bold;
}

.main-content {
    flex: 1;
    overflow-y: auto;
    background-color: #f5f5f5;
}

.player-bar-fixed {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 70px;
    background-color: #ffffff;
    border-top: 1px solid #e0e0e0;
    z-index: 1000;
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
