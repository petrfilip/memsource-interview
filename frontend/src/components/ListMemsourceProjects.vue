<script>
export default {
  data() {
    return {
      data: {
        id: '' || localStorage.getItem('userId'),
        userName: '' || localStorage.getItem('userName'),
        listProjects: [],
        errorMessage: undefined
      },
    }
  },
  methods: {},
  async mounted() {

    try {
      const httpResponse = await fetch("http://localhost:8080/v1/memsource/projects", {
        method: "GET",
        headers: {
          'X-userId': this.data.id
        }
      });

      const jsonResponse = await httpResponse.json();
      if (httpResponse.status >= 200 && httpResponse.status <= 299) {
        this.data.listProjects = jsonResponse.content;
        this.data.errorMessage = '';
      } else {
        this.data.errorMessage = jsonResponse.error + " - " + jsonResponse.message;
      }
    } catch (e) {
      this.data.errorMessage = e.message;
    }
  }
}
</script>

<template>
  <h1>List memsource projects</h1>
  {{ this.data.id && `User ${this.data.userName} registered under ${this.data.id} identifier` }}
  <hr>

  <div class="error" v-if="this.data.errorMessage">{{ this.data.errorMessage }}</div>

  <div class="tableWrapper">

    <ul class="header">
      <li>
        Name
      </li>
      <li>
        Status
      </li>
      <li>
        Source language
      </li>
      <li>
        Target languages
      </li>
    </ul>

    <ul v-for="item in this.data.listProjects" :key="item.name">
      <li>
        {{ item.name }}
      </li>
      <li>
        {{ item.status }}
      </li>
      <li>
        {{ item.sourceLang }}
      </li>
      <li>
        {{ item.targetLangs.join(', ') }}
      </li>
    </ul>
  </div>

</template>

<style>
.header {
  font-weight: bold;
}

.error {
  background-color: crimson;
  color: white;
}

.tableWrapper {
  border: 1px solid #333333
}

ul {
  padding: 5px;
  list-style-type: none;
  width: 100%;
  display: table;
  table-layout: fixed;
}

li {
  display: table-cell;
  width: 25%;
}

</style>