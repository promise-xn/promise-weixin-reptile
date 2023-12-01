
<template>
  <el-container>
    <el-header class="layout-header">

      <div>
        <h1 style="text-align: left; float: left">微信爬取</h1>
        <el-dropdown style="float:right;">
          <span class="el-dropdown-link">
            <el-link href="https://gitee.com/promise-xn/promise-weixin-reptile-demo" type="warning">gitee地址</el-link>
          </span>
        <span class="el-dropdown-link">
          Promise
        </span>
          <el-dropdown-menu></el-dropdown-menu>
        </el-dropdown>
      </div>

    </el-header>
    <el-container class="layout-body">
      <el-aside width="200px" class="layout-aside">
        <el-menu
            router
            :default-active="$route.path"
            class="el-menu-vertical-demo"
            background-color="#a1887f"
            text-color="#efebe9"
            active-text-color="#5d4037"
        >

<!--          <el-submenu index="1">-->
<!--            <template tabindex="/admin/home" slot="title">-->
<!--              <i class="el-icon-s-platform"></i>-->
<!--              <span>后台设置</span>-->
<!--            </template>-->
<!--&lt;!&ndash;            <el-menu-item index="/admin/home"><i class="el-icon-s-data"></i>首页</el-menu-item>&ndash;&gt;-->
<!--          </el-submenu>-->

          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-s-platform"></i>
              <span>公众号文章</span>
            </template>
            <el-menu-item index="/admin/news"><i class="el-icon-s-data"></i>公众号文章</el-menu-item>
            <el-menu-item index="/admin/category"><i class="el-icon-s-data"></i>公众号分类</el-menu-item>
          </el-submenu>


        </el-menu>
      </el-aside>
      <el-main style="background-color: #ededed;margin: 0"><router-view/></el-main>
    </el-container>
  </el-container>
</template>

<style>
.el-menu-item{
  color: #fff !important;
  background-color: #5d4037 !important;
}
.el-menu-item:hover{
  color: #fff !important;
  background-color: #4e342e !important;
}
.el-menu-item.is-active{
  color: #fff !important;
  background-color: #4e342e !important;
}
*{
  margin: 0;
}
.layout-header{
  background: #5d4037; color: #fff; line-height: 60px;
}
.layout-aside{
  background: #5d4037;
  color: #fff;
}
.layout-body{
  position: absolute;
  top: 60px;
  left: 0;
  bottom: 0;
  right: 0;
}
.el-dropdown-link {
  cursor: pointer;
  color: #fff;
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>


<script>


export default {
  data() {
    return {
      activeIndex: '1',
      activeIndex2: '1'
    };
  },
  methods: {

  }
}
</script>
