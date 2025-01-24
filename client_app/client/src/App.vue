<template>
    <v-layout>
        <v-app-bar color="primary" prominent>
            

            <v-toolbar-title>6Post</v-toolbar-title>

            <v-spacer></v-spacer>
        </v-app-bar>

        <v-navigation-drawer  app>
            <v-list density="compact">
                <v-divider></v-divider>
                <v-list-item link to="/page/themes" title="Themes"></v-list-item>
                <v-list-item v-if="!IsLoggedIn"  link to="/page/login" title="Login"></v-list-item>
                <v-list-item  v-if="!IsLoggedIn" link to="/page/register" title="Register"></v-list-item>
                <v-list-item  v-if="IsLoggedIn" link to="/page/personal" title="Personal"></v-list-item>
                <v-list-item  v-if="IsLoggedIn"  @click="logout" title="Logout"></v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-main style="min-height: 200px"> <router-view /></v-main>
    </v-layout>
</template>

<script>
    import router from './router'
    export default {
        methods: {
            logout() {
                if (confirm("Log out?") == true) {
                    localStorage.removeItem("token")
                    localStorage.removeItem("username")
                    router.push({ path: '/page/themes' }).then(() => {
                        location.reload();
                    });
                } else {
                    router.push({ path: '/page/themes' }).then(() => {
                        location.reload();
                    });
                    router.push({ path: '/page/themes' })

                }
            }
        },
        computed: {
            IsLoggedIn() {
                const token = localStorage.getItem('token');
                if (token == null) {
                    return false;
                }
                return true;
            },
        }, 
    }
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>

