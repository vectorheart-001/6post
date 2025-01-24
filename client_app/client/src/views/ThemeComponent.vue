<template>
    <v-container>
        <v-row justify="end">
            <v-col cols="auto">
                <v-btn color="primary" @click="showForm = !showForm">
                    {{ showForm ? 'Close Form' : 'Create Theme' }}
                </v-btn>
            </v-col>
        </v-row>

        <v-expand-transition>
            <v-card v-if="showForm">
                <v-card-title>Create Theme</v-card-title>
                <v-card-text>
                    <v-text-field v-model="newTheme.name" label="Name" required></v-text-field>
                    <v-textarea v-model="newTheme.description" label="Description"></v-textarea>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" @click="createTheme" :loading="loading">Create</v-btn>
                </v-card-actions>
                <v-alert v-if="createSuccessMessage" type="success" dismissible>
                    {{ createSuccessMessage }}
                </v-alert>
                <v-alert v-if="createErrorMessage" type="error" dismissible>
                    {{ createErrorMessage }}
                </v-alert>
            </v-card>
        </v-expand-transition>

        <v-row v-if="themes.length > 0" dense>
            <v-col v-for="theme in themes" :key="theme.id" cols="12" sm="6" md="4" lg="3">
                <v-card @click="loadTheme(theme.id)" class="ma-2">
                    <v-card-title>{{ theme.name }}</v-card-title>
                    <v-card-text>{{ theme.description }}</v-card-text>
                </v-card>
            </v-col>
        </v-row>
        <v-card v-else>
            <v-card-text>No themes available.</v-card-text>
        </v-card>
        <v-alert v-if="fetchErrorMessage" type="error" dismissible>
            {{ fetchErrorMessage }}
        </v-alert>
    </v-container>
</template>


<script>
    import router from '../router'
    export default {

        data() {
            return {
                newTheme: { 
                    name: '',
                    description: '',
                },
                themes: [],
                loading: false,
                createSuccessMessage: null,
                createErrorMessage: null,
                fetchErrorMessage: null,
                showForm: false,
            };
        },
        mounted() {
            this.fetchThemes();
        },
        methods: {
            async fetchThemes() {
                this.fetchErrorMessage = null;
                try {
                    const response = await fetch('http://localhost:8080/api/theme/get-all');
                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.themes = await response.json();
                } catch (error) {
                    console.error('Error fetching themes:', error);
                    this.fetchErrorMessage = `Error fetching themes: ${error.message}`;
                }
            },
            async createTheme() {
                this.loading = true;
                this.createSuccessMessage = null;
                this.createErrorMessage = null;
                try {
                    const response = await fetch('http://localhost:8080/api/theme/create', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(this.newTheme),
                    });

                    if (!response.ok) {
                        const errorData = await response.json();
                        const errorMessage = errorData.message || `HTTP error ${response.status}`;
                        throw new Error(errorMessage);
                    }
                    this.createSuccessMessage = "Theme created successfully!";
                    this.showForm = false;
                    this.fetchThemes(); 
                } catch (error) {
                    this.createErrorMessage = `Error: ${error.message || 'Failed to create theme'}`;
                } finally {
                    this.loading = false;
                }
            },
            loadTheme(themeId) {
                router.push({
                    name: 'themePostComponent', 
                    params: { themeid: themeId } 
                });
            }
        },
    };
</script>