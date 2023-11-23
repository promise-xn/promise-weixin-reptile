<template>
  <el-container>
    <el-header style="height: 30px">
      <meta name="referrer" content="never">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>文章列表</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>


    <el-main style="padding: 0">
      <div>
        <el-button @click="showImage = true;getImage()" style="margin: 10px">生成登录二维码</el-button>
        <el-dialog :visible.sync="showImage" title="二维码" :before-close="handleClose">
          <h2>扫码后请点击下方已扫码登录按钮</h2>
          <img :src="codeUrl" alt="二维码"/>
          <span slot="footer" class="dialog-footer">
        <el-button @click="showImage=false">取消</el-button>
        <el-button type="primary" @click="showImage=false;askQRCode()">已扫码登录</el-button>
      </span>
        </el-dialog>
        <el-button @click="dialogFormVisible = true;start()" style="margin: 10px">爬取文章</el-button>
      </div>

      <el-table
          v-loading="loading"
          :data="tableData"
          style="width: 100%;">
        <el-table-column type="index" label="编号" width="140"></el-table-column>

        <el-table-column label="分类名称" width="150">
          <template slot-scope="scope">
            <span>{{ scope.row.classifyName }}</span>
          </template>
        </el-table-column>

        <el-table-column label="标题" width="230" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-link :href="scope.row.link" type="warning">{{ scope.row.title }}</el-link>
          </template>
        </el-table-column>

        <el-table-column label="封面图" width="180">
          <template slot-scope="scope">
            <!--            <span>{{scope.row.coverImage}}</span>-->
            <el-image
                style="width: 100px; height: 100px"
                :src="scope.row.coverImage"
                :fit="fit"></el-image>
          </template>
        </el-table-column>


        <el-table-column
            label="发布时间"
            width="150">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ scope.row.publishDate }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="handleDetail(scope.$index, scope.row)">查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="pageNum"
          :page-size="10"
          background
          layout="prev, pager, next"
          :total=total>
      </el-pagination>
    </el-main>

  </el-container>
</template>

<style>
.el-table .cell {
  text-align: center;
}
body {
  margin: 0;
}
</style>

<script>
import axios from "axios";


export function getCaptcha() {
  return axios.request({
    url: "http://localhost:8088/reptile/wxArticle/getImage",
    method: "get",
    responseType: "blob" //这个非常重要，配置blob类型【第一个方法必须配置，第二个不用配置】
  });
}

export default {
  data() {
    return {
      showImage: false,
      codeUrl: "",
      tableData: [],
      loading: false,
      dialogFormVisible: false,
      form: {},
      pageNum: 1, // 当前页 默认设值1
      pageSize: 10, // 每页显示条目 默认设置5
      total: '',// 条目总数
      formLabelWidth: '120px',
      fits: ['fill', 'contain', 'cover', 'none', 'scale-down'],
      imageUrl: '',
    }

  },
  created() {
    this.loadPage(this.pageNum, this.pageSize);
    // this.loadAll();
  },
  // mounted() {
  //   // 在组件挂载后调用接口
  //   this.loadPage(this.pageNum,this.pageSize);
  // },
  methods: {
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadPage(this.pageNum, this.pageSize);
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPage(this.pageNum, this.pageSize);
      console.log(`当前页: ${val}`);
    },

    loadPage(pageNum, pageSize) {
      this.loading = true
      let url = 'http://localhost:8088/reptile/wxArticle/pages';
      axios.get(url, {
        params: {
          pageNum,
          pageSize
        }
      }).then(resp => {
        console.log(resp);
        resp.data
        this.tableData = resp.data.record;
        this.total = resp.data.total;
        this.loading=false
        // this.total = resp.data.list.total;

      });
    },

    handleDetail(index, row) {
      console.log(row.id);
      // let url = 'http://localhost:8088/reptile/wxArticle/'+row.id;
      this.$router.push({
        path: 'newsDetail',
        query: {
          id: row.id,
        }
      })
    },
    // getImages:{
    //   url:'http://localhost:8088/reptile/wxArticle/getImage',
    //       method:'GET',
    //       responseType : 'blob'
    // },

    getImage() {
      getCaptcha().then((res) => {
        console.log(res)
        this.codeUrl = window.URL.createObjectURL(res.data) //获取图片流的路径
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done(); // 点击确定，关闭对话框
          })
          .catch(_ => {
          }); // 点击取消，不关闭对话框
    },
    askQRCode() {
      axios.get('http://localhost:8088/reptile/wxArticle/askQRCode')
          .then(response => {
            if (response.status === 200) {
              // 请求成功
              alert(response.data.message)
            } else {
              // 请求失败
              alert(response.data.message)
            }
          }).catch(error => {
        console.error(error)
        // 请求错误
        alert('获取数据出现错误！')
      })
    },
    start(){
      this.loading=true
      axios.post('http://localhost:8088/reptile/wxArticle/start')
      .then(resp=>{
        if (resp.status === 200) {
          // 请求成功
          alert(resp.data.message)
        } else {
          // 请求失败
          alert(resp.data.message)
        }
        this.loading=false
        this.loadPage(this.pageNum, this.pageSize);
      })
    }
  }
}
</script>