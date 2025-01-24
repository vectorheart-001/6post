<template>
    <v-container fluid class="fill-height">
        <v-snackbar v-model="snack" transition="true" timeout="2000" color="green" absolute top outlined right>
            <strong>
                Registered Successfully!
            </strong>
        </v-snackbar>
        <v-row align="center" justify="center">
            <v-col cols="12" sm="8" md="6">
                <v-card class="elevation-12">
                    <v-card-title class="headline">Register</v-card-title>
                    <v-card-text>
                        <v-form ref="form" v-model="valid" lazy-validation>
                            <v-text-field v-model="username" label="Username" :rules="usernameRules" required></v-text-field>
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
                        <v-btn color="primary" @click="register" :loading="loading" :disabled="!valid">Register</v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import router from '../router';

    export default {
        data: () => ({
            valid: true,
            username: '',
            usernameRules: [v => !!v || 'Username is required'],
            password: '',
            passwordRules: [v => !!v || 'Password is required'],
            show: false,
            loading: false,
            error: null,
            snack: false,
        }),
        methods: {
            register() {
                const instance = this;
                if (this.$refs.form.validate()) {
                    this.loading = true;
                    this.error = null;

                    fetch('http://localhost:8080/api/auth/register', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({ username: this.username, password: this.password }),
                    })
                        .then(response => {
                            if (response.ok) {
                                instance.snack = true;
                                setTimeout(() => {
                                    router.push({ path: '/page/login' }).then(() => {
                                        router.go(0);
                                    });
                                }, 2000)

                            } else {
                                response.json().then(errorData => {
                                    instance.error = errorData.message || `Registration failed`;
                                }).catch(() => {
                                    instance.error = `Registration failed`;
                                });
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            this.error = error.message || 'Registration failed';
                        })
                        .finally(() => {
                            this.loading = false;
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