<script setup>
import { RouterLink, RouterView } from 'vue-router';
import PlayerBar from '@/components/PlayerBar.vue';
</script>

<template>
    <div class="layout-container">
        <header class="header">
            <div class="logo">冈易云音乐</div>
            <nav class="navigation">
                <RouterLink to="/" class="nav-link">发现音乐</RouterLink>
                <RouterLink to="/search" class="nav-link">搜索</RouterLink>
            </nav>
        </header>

        <main class="main-content">
            <RouterView />
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
    /* 为播放栏留出空间 */
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
    /* 播放栏的高度 */
    background-color: #ffffff;
    border-top: 1px solid #e0e0e0;
    z-index: 1000;
    /* 确保播放栏在最上层 */
}
</style>
