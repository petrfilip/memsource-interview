<script setup>
import {RouterLink, RouterView} from "vue-router";</script>

<template>
  <div class="wrapper">

    <header>

      <nav>
        <RouterLink to="/">Setup account</RouterLink>
        <RouterLink to="/listProjects">List projects</RouterLink>
      </nav>

    </header>

    <RouterView/>
  </div>
</template>

<style>
body {
  background-color: #f2f2f2;

}

.wrapper {
  margin: auto;
  width: 50%;
  background-color: #fff;
  padding: 10px;
}

nav {
  background-color: #333;
  overflow: hidden;
}

nav a {
  float: left;
  color: #f2f2f2;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
}

nav a.router-link-active {
  text-decoration: underline;
}

input, button {
  box-sizing: content-box;
  padding: 10px;
  width: 250px
}

button {
  margin-top: 10px;
  box-sizing: content-box;
  padding: 10px;
  width: 250px
}
</style>
