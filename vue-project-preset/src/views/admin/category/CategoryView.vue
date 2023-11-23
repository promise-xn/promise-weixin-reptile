<template>
  <el-container>
    <el-header style="height: 30px">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/admin/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>分类列表</el-breadcrumb-item>
      </el-breadcrumb>
    </el-header>
    <el-main style="padding: 0">
      <div>
        <!-- Form -->
        <el-button @click="addCategory = true" style="margin: 10px">添加公众号</el-button>

        <el-dialog title="添加公众号" :visible.sync="addCategory" width="35%">
          <el-form :model="a" style="width: 400px">
            <el-form-item label="公众号名" :label-width="formLabelWidth">
              <el-input v-model="a.name"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="addCategory = false">取 消</el-button>
            <el-button type="primary" @click="addCategory = false; submit()">确 定</el-button>
          </div>
        </el-dialog>
      </div>

      <el-table
          :data="categoryData"
          style="width: 100%;">
        <el-table-column type="index" label="编号" width="300"></el-table-column>

        <el-table-column label="公众号名称" width="400">
          <template slot-scope="scope">
            <span>{{ scope.row.classifyName }}</span>
          </template>
        </el-table-column>


        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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

<script>
import axios from "axios";

export default {
  data() {
    return {
      categoryData: [],
      addCategory: false,
      update: false,
      u: {},
      a:{
        name:'',
      },
      formLabelWidth: '120px',
      pageNum: 1, // 当前页 默认设值1
      pageSize: 10, // 每页显示条目 默认设置5
      total: '' ,// 条目总数
      fits: ['fill', 'contain', 'cover', 'none', 'scale-down'],
    }
  },
  created(){
    this.loadPage(this.pageNum,this.pageSize);
    // this.loadAll();
  },
  mounted() {
    // 在组件挂载后调用接口
    this.loadPage(this.pageNum,this.pageSize);
  },
  methods: {
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadPage(this.pageNum,this.pageSize);
      console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPage(this.pageNum,this.pageSize);
      console.log(`当前页: ${val}`);
    },

    loadPage(pageNum,pageSize) {
      let url = 'http://localhost:8088/reptile/wxArticleClassify/pages';
      axios.get(url,{
        params:{
          pageNum,
          pageSize
        }
      }).then(resp => {
        console.log(resp);
        resp.data
        this.categoryData = resp.data.record;
        this.total = resp.data.total;
        // this.total = resp.data.list.total;

      });
    },


    handleEdit(index, row) {
      console.log(index, row);
    },
    handleDelete(index, row) {
      axios.delete("http://localhost:8088/reptile/wxArticleClassify/del/"+row.id)
      .then(resp=>{
        alert(resp.data.data)
        this.loadPage(this.pageNum,this.pageSize);
      })
    },
    submit(){
      console.log(this.a.name)
      let param = new URLSearchParams()
      param.append('classifyName', this.a.name)

      axios.post("http://localhost:8088/reptile/wxArticleClassify/add",param).then(resp=>{
        this.a.name = ''
        alert(resp.data.data)
        this.loadPage(this.pageNum,this.pageSize);
      })
    },
    update(){

    }
  }
}
</script>

<style>
.el-table .cell{
  text-align: center;
}
</style>