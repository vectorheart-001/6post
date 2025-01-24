<template>
    <v-container>
        <div v-if="posts.length > 0" class="reddit-post-container">
            <h2 class="reddit-post-title">Your Posts</h2>
            <div v-for="post in posts" :key="post.id" class="reddit-post">
                <div class="reddit-post-header">
                    <div class="post-info">
                        <div class="post-title">{{ post.title }}</div>
                    </div>
                    <div class="post-actions">
                        <v-dialog v-model="editDialog" max-width="500px">
                            <template v-slot:activator="{ props }">
                                <v-btn v-bind="props" color="primary" @click="editPost(post)">
                                    Edit
                                </v-btn>
                            </template>
                            <v-card>
                                <v-card-title>
                                    <span class="text-h5">Edit Post</span>
                                </v-card-title>
                                <v-card-text>
                                    <v-text-field v-model="editingPost.title" label="Title"></v-text-field>
                                    <v-textarea v-model="editingPost.description" label="Description"></v-textarea>
                                </v-card-text>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="blue-grey darken-1" text @click="editDialog = false">
                                        Close
                                    </v-btn>
                                    <v-btn color="blue darken-1" text @click="saveEdit">
                                        Save
                                    </v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                        <v-btn color="error" @click="deletePost(post.id)">
                            Delete
                        </v-btn>
                    </div>
                </div>
                <div class="reddit-post-content">
                    {{ post.description }}
                </div>
            </div>
        </div>
        <v-card-text v-else>You have no posts yet.</v-card-text>
        <v-alert v-if="fetchErrorMessage" type="error" dismissible>
            {{ fetchErrorMessage }}
        </v-alert>
        <v-alert v-if="editErrorMessage" type="error" dismissible>
            {{ editErrorMessage }}
        </v-alert>
        <v-alert v-if="deleteErrorMessage" type="error" dismissible>
            {{ deleteErrorMessage }}
        </v-alert>
    </v-container>
</template>


<script>
    export default {
        data() {
            return {
                posts: [],
                fetchErrorMessage: null,
                editErrorMessage: null,
                deleteErrorMessage: null,
                editDialog: false,
                editingPost: {
                    title: '',
                    description: '',
                },
                currentEditPost: 0,
                searchTerm:''
            };
        },
        mounted() {
            this.fetchUserPosts();
        },
        methods: {
            async fetchUserPosts() {
                const token = localStorage.getItem('token');
                if (!token) {
                    return;
                }
                this.fetchErrorMessage = null;
                try {
                    const response = await fetch('http://localhost:8080/api/post/get_user_posts', {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });
                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.posts = await response.json();
                } catch (error) {
                    this.fetchErrorMessage = `Error fetching posts: ${error.message}`;
                }
            },
            editPost(post) {
                this.editingPost = { ...post };
                this.currentEditPost = post.id
                this.editDialog = true;
            },
            async saveEdit() {
                const token = localStorage.getItem('token');
                if (!token) {
                    return;
                }
                this.editErrorMessage = null;
                try {
                    const response = await fetch(`http://localhost:8080/api/post/edit_post/${this.currentEditPost}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${token}`,
                        },
                        body: JSON.stringify(this.editingPost),
                    });
                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.editDialog = false;
                    this.fetchUserPosts();
                } catch (error) {
                    this.editErrorMessage = `Error editing post: ${error.message}`;
                }
            },
            async deletePost(postId) {

                if (confirm("Delete this post?") == true) {
                    console.log();
                } else {
                    return;
                }

                const token = localStorage.getItem('token');
                if (!token) {
                    return;
                }
                this.deleteErrorMessage = null;
                try {
                    const response = await fetch(`http://localhost:8080/api/post/delete_post?id=${postId}`, {
                        method: 'DELETE',
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });
                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.fetchUserPosts();
                } catch (error) {
                    this.deleteErrorMessage = `Error deleting post: ${error.message}`;
                }
            },
            async searchPosts() {
                let url = `http://localhost:8080/api/post/search?searchTerm=${this.searchTerm}`;
                if (this.searchTerm.length === 0) {
                    this.fetchUserPosts();
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
    }

    .reddit-post-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 0.5rem;
    }

    .post-actions {
        display: flex;
        gap: 10px;
    }

    .reddit-post-content {
        font-size: 1rem;
        word-wrap: break-word;
        text-align: left;
        margin-top: 10px;
    }

    .post-actions {
        display: flex;
    }
</style>


