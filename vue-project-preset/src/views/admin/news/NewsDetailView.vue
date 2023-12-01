<template>
    <el-container>
      <el-header>
        <meta name="referrer" content="never">
      </el-header>
      <el-main>
<!--        <h1>标题：{{ detail.content }}</h1>-->

        <div v-html="htmlContent"></div>
      </el-main>
    </el-container>
</template>

<script>

import axios from "axios";


export default {
  data() {
    return {
      id: '',
      htmlContent: ''
    };
  },
  created() {
    this.findDetail();
  },

  methods:{
    findDetail() {
      const params = this.$route.query;
      this.id = params.id;
      let url = 'http://localhost:8088/reptile/wxArticle/'+this.id;
      axios.get(url)
      .then(resp=>{
        this.htmlContent = resp.data.data.content;
      })
    },
  }
}

</script>

<style scoped>

</style>