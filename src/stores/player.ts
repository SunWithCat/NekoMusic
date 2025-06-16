import { defineStore} from 'pinia'

export const usePlayerStore = defineStore('player', {
    state:() => ({
        playlist: [] as any[],
        currentIndex: -1,
        isPlaying: false,
        currentTime: 0,
        duration: 0
    }),

    getters: {
        currentSong(state): any {
            return state.playlist[state.currentIndex] || null
        }
    },

    actions: {
        setPlaylist(songs: any[], startIndex: number = 0) {
            this.playlist = songs;
            this.currentIndex = startIndex;
            this.duration = this.currentSong?.duration || 0;
            this.currentTime = 0;
            this.isPlaying = true; 
        },

        playNext() {
            if (this.playlist.length > 0) {
                this.currentIndex = (this.currentIndex + 1) % this.playlist.length;
                this.duration = this.currentSong?.duration || 0;
                this.currentTime = 0; // 重置当前时间
                this.isPlaying = true; 
            }
        },

        playPrev() {
            if (this.playlist.length > 0) {
                this.currentIndex = (this.currentIndex - 1 + this.playlist.length) % this.playlist.length;
                this.duration = this.currentSong?.duration || 0;
                this.currentTime = 0; // 重置当前时间
                this.isPlaying = true; 
            }
        },

        togglePlayStatus() {
            if(this.currentSong) {
                this.isPlaying = !this.isPlaying
            }
        },

        updateCurrentTime(time: number){
            this.currentTime = time
        },

        updateDuration(time: number) {
            console.log("更新播放时长为:", time); // 添加日志
            this.duration = time
        },

        seek(time: number) {
            this.currentTime = time
        }
    }
})
