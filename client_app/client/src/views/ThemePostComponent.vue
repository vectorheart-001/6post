<template>
    <v-container>
        <v-row justify="end">
            <v-col cols="auto">
                <v-btn color="primary" @click="showForm = !showForm">
                    {{ showForm ? 'Close Form' : 'Create Post' }}
                </v-btn>
            </v-col>
        </v-row>

        <v-expand-transition>
            <v-card v-if="showForm">
                <v-card-title>Create Post</v-card-title>
                <v-card-text>
                    <v-text-field v-model="newPost.title" label="Title" required></v-text-field>
                    <v-textarea v-model="newPost.description" label="Description"></v-textarea>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" @click="createPost" :loading="loading">Create</v-btn>
                </v-card-actions>
                <v-alert v-if="createSuccessMessage" type="success" dismissible>
                    {{ createSuccessMessage }}
                </v-alert>
                <v-alert v-if="createErrorMessage" type="error" dismissible>
                    {{ createErrorMessage }}
                </v-alert>
            </v-card>
        </v-expand-transition>

        <v-text-field v-model="searchTerm" label="Search Posts" @input="searchPosts"></v-text-field>

        <div v-if="posts.length > 0" class="reddit-post-container">
            <h2 class="reddit-post-title">Posts</h2>
            <div v-for="post in posts" :key="post.id" class="reddit-post">
                <div class="reddit-post-header">
                    <div class="post-info">
                        <div v-if="post.author" class="post-author">By: {{ post.author }}</div>
                        <div class="post-title">{{ post.title }}</div>
                    </div>
                </div>
                <div class="reddit-post-content">
                    {{ post.description }}
                </div>
            </div>
        </div>
        <v-card-text v-else>No posts found.</v-card-text>
        <v-alert v-if="fetchErrorMessage" type="error" dismissible>
            {{ fetchErrorMessage }}
        </v-alert>
    </v-container>
</template>

<script>

    import { useRoute } from "vue-router";
    export default {
        data() {
            return {
                newPost: {
                    title: '',
                    description: '',
                },
                themes: [],
                posts: [],
                loading: false,
                createSuccessMessage: null,
                currentTheme: null,
                createErrorMessage: null,
                fetchErrorMessage: null,
                searchTerm: '',
                showForm: false,
            };
        },
        mounted() {
            const routeCatch = useRoute()
            this.currentTheme = routeCatch.params.themeid
            this.fetchPostsByTheme(this.currentTheme);
        },
        methods: {
            async fetchPostsByTheme(themeId) {
                this.fetchErrorMessage = null;
                try {
                    const response = await fetch(`http://localhost:8080/api/post/get_by_theme/${themeId * 1}`);
                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.posts = await response.json();
                } catch (error) {
                    console.error('Error fetching posts:', error);
                    this.fetchErrorMessage = `Error fetching posts: ${error.message}`;
                }
            },
            async createPost() {
                this.loading = true;
                this.createSuccessMessage = null;
                this.createErrorMessage = null;
                try {
                    const token = localStorage.getItem('token');
                    if (!token) {
                        throw new Error("Please login.");
                    }
                    const response = await fetch(`http://localhost:8080/api/post/create_post/${this.currentTheme}`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`,
                        },
                        body: JSON.stringify(this.newPost),
                    });

                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.showForm = false;
                    this.fetchPostsByTheme(this.currentTheme);
                } catch (error) {
                    this.createErrorMessage = `Error: ${error.message || 'Failed to create post'}`;
                } finally {
                    this.loading = false;
                }
            },
            async searchPosts() {
                let url = `http://localhost:8080/api/post/search?searchTerm=${this.searchTerm}&themeId=${this.currentTheme}`;
                if (this.searchTerm.length === 0) {
                    this.fetchPostsByTheme(this.currentTheme);
                    return;
                }
                this.fetchErrorMessage = null;
                try {
                    const response = await fetch(url);
                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.posts = await response.json();
                } catch (error) {
                    console.error('Error searching posts:', error);
                    this.fetchErrorMessage = `Error searching posts: ${error.message}`;
                }
            },
        },
    };
</script>


<style scoped>
    .reddit-post-container {
        padding: 1rem;
    }

    .reddit-post {
        border: 1px solid #ddd;
        border-radius: 4px;
        margin-bottom: 1rem;
        padding: 1rem;
        overflow: hidden;
    }

    .reddit-post-header {
        display: flex;
        align-items: flex-start;
        margin-bottom: 0.5rem;
    }

    .post-info {
        display: flex;
        flex-direction: column; /* Stack author and title vertically */
    }

    .post-author {
        font-size: 0.8rem; /* Smaller font for the author */
        color: #777; /* Grayish color for the author */
        margin-bottom: 0.2rem;
    }

    .post-title {
        font-weight: bold;
    }

    .reddit-post-content {
        font-size: 1rem;
        word-wrap: break-word;
        text-align: left;
    }
</style>