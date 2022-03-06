import {createRouter, createWebHistory} from "vue-router";
import SetupView from "../views/SetupAccountView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "setup",
      component: SetupView,
    },
    {
      path: "/listProjects",
      name: "listProjects",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/ListProjectsView.vue"),
    },
  ],
});

export default router;
