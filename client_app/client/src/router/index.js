import { createRouter, createWebHashHistory } from 'vue-router';
//import HomeView from '../views/HomeView.vue'; 
import LoginComponent from '../views/LoginComponent.vue'
import RegisterComponent from '../views/RegisterComponent.vue'
import ThemeComponent from '../views/ThemeComponent.vue'
import ThemePostComponent from '../views/ThemePostComponent.vue'
import PersonalComponent from '../views/PersonalComponent.vue'

const routes = [
    //{
    //    path: '/',
    //    name: 'home',
    //    component: HomeView,
    //},
    {
        path: '/about',
        name: 'about',
        component: () => import( '../views/AboutView.vue'),
    },
    {
        path: '/page/login',
        name: 'login',
        component: LoginComponent, 
    },
    {
        path: '/page/register',
        name: 'register',
        component: RegisterComponent,
    },
    {
        path: '/page/themes',
        name: 'theme',
        component: ThemeComponent,
    },
    {
        path: '/page/posts/:themeid',
        name: 'themePostComponent',
        component: ThemePostComponent
    },
    {
        path: '/page/personal',
        name: 'personal',
        component: PersonalComponent,
    },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

export default router;
