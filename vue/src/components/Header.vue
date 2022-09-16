<template>
    <div style="line-height: 60px ;display: flex">
        <div style="flex: 1;">
            <span :class="collapseBtnClass" style="cursor: pointer ;font-size: 17px" @click="collapse"></span>
            <el-breadcrumb separator="/" style="display: inline-block;margin-left: 10px">
                <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
                <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
            </el-breadcrumb>

        </div>
        <el-dropdown style="width: 70px;cursor: pointer">
            <div style="display: inline-block">
                <img :src="user.avataraUrl" alt=""
                     style="width: 5px; border-radius: 50%; position: relative;top: 10%;right: 5px">
                <span>{{user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px;text-align: center">
                <el-dropdown-item>
                    <router-link to="/person" style="text-decoration: none">个人信息</router-link>
                </el-dropdown-item>
                <el-dropdown-item>
                    <span style="text-decoration: none" @click="logout">退出</span>
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
    export default {
        name: "Header",
        props: {
            collapseBtnClass: String,
        },
        computed: {
            currentPathName() {
                return this.$store.state.currentPathName; //需要监听的数据
            }
        },
        data() {
            return {
                user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
            }
        },
        methods: {
            collapse() {//折叠
                this.$emit("asideCollapse")
            },
            logout() {
                this.$router.push("/login")
                localStorage.removeItem("user")
                this.$message.success("退出成功")
            }
        }
    }
</script>

<style scoped>

</style>