import { defineStore} from 'pinia'

export const usePlayerStore = defineStore('player', {
    state:() => ({
        playlist: [] as any[],
        currentIndex: -1,
        isPlaying: false,
    }),

    getters: {
        currentSong(state): any {
            return state.playlist[state.currentIndex] || null
        }
    },

    actions: {
        setPlaylist(songs: any[],startIndex:number = 0) {
            this.playlist = songs
            this.currentIndex = startIndex
            this.isPlaying = true
        },

        playNext() {
            if(this.playlist.length > 0) {
                this.currentIndex = (this.currentIndex + 1) % this.playlist.length
                this.isPlaying = true
            }
        },

        playPrev() {
            if(this.playlist.length > 0) {
                this.currentIndex = (this.currentIndex - 1) % this.playlist.length
                this.isPlaying = true
            }
        },

        togglePlayStatus() {
            if(this.currentSong) {
                this.isPlaying = !this.isPlaying
            }
        }
    }
})