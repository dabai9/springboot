<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-search"
                      v-model="username"></el-input>
            <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5"
                      v-model="email"></el-input>
            <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-location-outline"
                      class="ml-5" v-model="address"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i>
            </el-button>
            <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='取消'
                    icon="el-icon-info"
                    icon-color="red"
                    title="这是一段内容确定删除吗？"
                    @confirm="delBatch"
            >
                <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i>
                </el-button>
            </el-popconfirm>
            <el-upload
                    action="http://localhost:9090/user/import"
                    :show-file-list="false" accept="xlsx" :on-success="handelExcelImportSuccess"
                    name="用户名"
                    style="display: inline-block">
                <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
            </el-upload>
            <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top"></i></el-button>
        </div>
        <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
                  @selection-change="handleSelectionChange">>
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column prop="id" label="Id" width="50"></el-table-column>
            <el-table-column prop="username" label="用户名" width="80"></el-table-column>
            <el-table-column prop="nickname" label="昵称" width="100"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="150"></el-table-column>
            <el-table-column prop="phone" label="电话" width="120"></el-table-column>
            <el-table-column prop="address" label="地址" width="160"></el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i>
                    </el-button>
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='取消'
                            icon="el-icon-info"
                            icon-color="red"
                            title="这是一段内容确定删除吗？"
                            @confirm="del(scope.row.id)"
                    >
                        <el-button type="danger" slot="reference">
                            删除<i class="el-icon-remove-outline"></i></el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
        <!--        新增弹窗-->
        <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="40%">
            <el-form label-width="80px" size="samil">
                <el-form-item label="用户名">
                    <el-input v-model="form.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="昵称">
                    <el-input v-model="form.nickname" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="form.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="form.phone" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="form.address" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: "User",
        data() {
            return {
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 5,
                username: "",
                email: "",
                address: "",
                form: {},
                dialogFormVisible: false,//弹窗默认不展示
                multipleSelection: [],
                headerBg: 'headerBg'
            }
        },
        created() {
            //请求分页查询的数据
            this.load()
        },
        methods: {
            load() {
                //请求分页查询数据
                this.request.get("/user/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        username: this.username,
                        email: this.email,
                        address: this.address
                    }
                }).then(res => {
                    console.log(res.data)
                    this.tableData = res.date.records
                    this.total = res.date.total
                })
            },
            save() {
                this.request.post("/user", this.form).then(res => {
                    if (res.data) {
                        this.$message.success("保存成功")
                        this.dialogFormVisible = false
                        this.load()
                    } else {
                        this.$message.error("保存失败")
                    }
                })
            },
            handleAdd() {//新增
                this.dialogFormVisible = true
                this.form = {}
            },
            handleEdit(row) {//编辑
                this.form = row
                this.dialogFormVisible = true
            },
            del(id) { //删除
                this.request.delete("/user/" + id).then(res => {
                    if (res.data) {
                        this.$message.success("删除成功")
                        this.load()
                    } else {
                        this.$message.error("删除失败")
                    }
                })
            },
            handleSelectionChange(val) {//批量删除
                this.multipleSelection = val
            },
            delBatch() {
                let ids = this.multipleSelection.map(v => v.id)//将id的对象数转成纯id的数组 [{},{}]=>[1,2]
                this.request.post("/user/del/batch", ids).then(res => {
                    if (res.data) {
                        this.$message.success("删除成功")
                        this.load()
                    } else {
                        this.$message.error("删除失败")
                    }
                })
            },
            reset() {//重置搜索
                this.username = ""
                this.email = ""
                this.address = ""
                this.load()
            },
            handleSizeChange(pageSize) {
                console.log(pageSize)
                this.pageSize = pageSize
                this.load()
            },
            handleCurrentChange(pageNum) {
                console.log(pageNum)
                this.pageNum = pageNum
                this.load()
            },
            //导出
            exp() {
                window.open("http://localhost:9090/user/export")
            },
            //文件上传成功的钩子
            handelExcelImportSuccess(){
                this.$message.success("文件导入成功!")
                this.load()
            }
        }
    }
</script>

<style>
    .headerBg {
        background: #eee !important;
    }
</style>