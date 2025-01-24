<template>
    <v-container fluid class="fill-height">
        <v-snackbar v-model="snack" transition="true" timeout="2000" color="green" absolute top outlined right>
            <strong>
                Logged in!
            </strong>
        </v-snackbar>

        <v-row align="center" justify="center">
            <v-col cols="12" sm="8" md="6">
                <v-card class="elevation-12">
                    <v-card-title class="headline">Login</v-card-title>
                    <v-card-text>
                        <v-form ref="form" v-model="valid" lazy-validation>
                            <v-text-field v-model="username"
                                          label="Username"
                                          :rules="usernameRules"
                                          required></v-text-field>

                            <v-text-field v-model="password"
                                          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
                                          :rules="passwordRules"
                                          :type="show ? 'text' : 'password'"
                                          label="Password"
                                          @click:append="show = !show"
                                          required></v-text-field>

                            <v-alert v-if="error" type="error" dismissible @click:close="error = null">
                                {{ error }}
                            </v-alert>
                        </v-form>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" @click="login" :loading="loading" :disabled="!valid">Login</v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import router from '../router'
    export default {
        data: () => ({
            valid: true,
            email: '',
            username:'',
            usernameRules: [
                v => !!v || 'Username is required',
            ],
            password: '',
            passwordRules: [
                v => !!v || 'Password is required',
            ],
            show: false,
            loading: false,
            error: null,
            snack:false
        }),
        methods: {
            login() {
                const instance = this;

                if (this.$refs.form.validate()) {
                    this.loading = true;
                    this.error = null; // Clear any previous errors

                    fetch('http://localhost:8080/api/auth/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({ username: this.username, password: this.password }),
                    })
                        .then(response => {
                            if (response.ok) {
                                response.text().then(function (text) {
                                    localStorage.setItem('token', text);
                                    localStorage.setItem('username', instance.username); // Corrected typo
                                    instance.snack = true;
                                    router.push({ path: '/page/themes' }).then(() => {
                                        router.go(0);
                                    });
                                });
                            } else {
                                // Handle non-2xx responses (errors)
                                response.json().then(errorData => { // Try to parse error message from server
                                    instance.error = errorData.message || `Login failed with status ${response.status}`;
                                }).catch(() => { // If parsing fails, use a generic message
                                    instance.error = `Invalid login `;
                                });
                            }
                        })
                        .catch(error => {
                            instance.error = error.message || 'Login failed'; // Handle network or other errors
                        })
                        .finally(() => {
                            instance.loading = false;
                        });
                }
            },
        },
    };
</script>

<style scoped>
    .fill-height {
        min-height: 100vh;
    }
</style>


