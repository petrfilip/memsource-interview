<script>
export default {
  data() {
    return {
      data: {
        username: '' || localStorage.getItem('userName'),
        password: '',
        id: '' || localStorage.getItem('userId'),
      },
    }
  },
  methods: {
    async handleSubmit() {

      try {
        const httpResponse = await fetch("http://localhost:8080/v1/memsource/users", {
          method: "POST",
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            userName: this.data.username,
            password: this.data.password
          })
        });
        const jsonResponse = await httpResponse.json();
        if (httpResponse.status >= 200 && httpResponse.status <= 299) {
          this.data.id = jsonResponse.id;
          localStorage.setItem('userId', jsonResponse.id);
          localStorage.setItem('userName', jsonResponse.userName);
          this.data.errorMessage = '';
        } else {
          this.data.errorMessage = jsonResponse.error + " - " + jsonResponse.message;
        }
      } catch (e) {
        this.data.errorMessage = e.message;
      }

    },
  },
}
</script>

<template>
  <h1>Setup memsource account</h1>
  {{ this.data.id && `User ${this.data.username} registered under ${this.data.id} identifier` }}
  <hr>

  <div class="error" v-if="this.data.errorMessage">{{ this.data.errorMessage }}</div>

  <FormKit type="form" v-model="data" @submit="handleSubmit">

    <FormKit
        type="text"
        label="Username"
        name="username"
        placeholder="Memsource username"
        validation="required|email"
        autocomplete="off"
    />
    <div class="double">
      <FormKit
          autocomplete="off"
          type="password"
          name="password"
          label="Password"
          placeholder="Memsource password"
          validation="required"
      />

    </div>
  </FormKit>

</template>

<style>
.error {
  background-color: crimson;
  color: white;
}
</style>