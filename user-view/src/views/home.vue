<template>
  <div class="box">
    <div class="box-top">
      <span class="left-span">用户列表</span>
      <el-button class="right-button" type="primary" @click="loginOut()" round
        >退出登录</el-button
      >
      <span class="right-span"> 当前用户：{{ localRealName }}</span>
    </div>
    <div class="box-body">
      <div class="select">
        <div class="demo-input-suffix">
          <p>用户名称：</p>
          <el-input
            placeholder="请输入内容"
            prefix-icon="el-icon-search"
            v-model="selectRealName"
          >
          </el-input>
        </div>
        <class class="demo-input-suffix">
          <p>请选择单位：</p>
          <el-select
            v-model="selectUnit"
            filterable
            :clearable="true"
            placeholder="请选择"
          >
            <el-option
              v-for="(item, index) in unitList"
              :key="index"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
        </class>
        <div class="right_div">
          <div class="add-button">
            <el-button
              class="select-button"
              type="primary"
              @click="getList"
              round
              size="small"
              >搜索
            </el-button>
          </div>
          <div class="add-button">
            <el-button
              class="select-button"
              @click="resetForm"
              round
              size="small"
              >重置
            </el-button>
          </div>
        </div>
      </div>
      <div class="add-button">
        <el-button
            v-show="check"
            class="select-button"
            type="primary"
            @click="handleAdd()"
            round
            size="small"
        >新增
        </el-button>
      </div>
      <div class="list">
        <el-table
          stripe
          :data="userList"
          @cell-dblclick="cellDlClick"
          style="width: 100%"
        >
          <el-table-column label="序号" type="index" align="center" width="230">
          </el-table-column>
          <el-table-column prop="unit" label="单位" align="center" width="230">
          </el-table-column>
          <el-table-column
            prop="realName"
            label="用户姓名"
            align="center"
            width="230"
          >
          </el-table-column>
          <el-table-column
            prop="gender"
            label="性别"
            align="center"
            width="230"
          >
          </el-table-column>
          <el-table-column prop="age" label="年龄" align="center" width="230">
          </el-table-column>
          <el-table-column prop="role" label="角色" align="center" width="230">
          </el-table-column>

          <!--          <el-table-column-->
          <!--              prop="onlineType"-->
          <!--              label="在线状态"-->
          <!--              width="230">-->
          <!--          </el-table-column>-->

          <el-table-column
            prop="onlineType"
            label="在线状态"
            align="center"
            width="230"
            filter-placement="bottom-end"
          >
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.onlineType === '离线' ? 'primary' : 'success'"
                disable-transitions
                >{{ scope.row.onlineType }}</el-tag
              >
            </template>
          </el-table-column>

          <el-table-column align="center" v-if="check" label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                v-show="check"
                type="danger"
                @click="handleDialog(scope.row)"
                >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
          <span>是否删除该数据</span>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleDelete()">确 定</el-button>
          </span>
        </el-dialog>
      </div>
      <div class="page">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 30, 40, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalPage"
        >
        </el-pagination>
      </div>
      <el-dialog title="用户详情" :visible.sync="dialogFormVisible">
        <!-- :rules="rules" -->
        <el-form
          :model="userDetail"
          :rules="rules"
          ref="userDetailRef"
          label-position="right"
        >
          <el-form-item label="用户类型" prop="userType">
            <el-radio-group v-model="userDetail.userType">
              <el-radio label="1">普通用户</el-radio>
              <el-radio label="0">管理员</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            label="用户名称"
            :label-width="formLabelWidth"
            prop="username"
          >
            <el-input
              v-model="userDetail.username"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="密码"
            :label-width="formLabelWidth"
            prop="password"
          >
            <el-input
              type="password"
              v-model="userDetail.password"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="真实姓名"
            :label-width="formLabelWidth"
            prop="realName"
          >
            <el-input
              v-model="userDetail.realName"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="年龄" :label-width="formLabelWidth" prop="age">
            <el-input v-model="userDetail.age" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="单位" :label-width="formLabelWidth" prop="unit">
            <el-input v-model="userDetail.unit" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色" :label-width="formLabelWidth" prop="role">
            <el-input v-model="userDetail.role" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="用户性别" prop="gender">
            <el-radio-group v-model="userDetail.gender">
              <el-radio label="1">男</el-radio>
              <el-radio label="2">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="userDetail.remark" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submit(userDetail)"
            >确 定</el-button
          >
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  getList,
  loginOut,
  deleteById,
  add,
  update,
  detail,
  getUnitList,
} from "@/api/home";

export default {
  name: "home",
  data() {
    return {
      userDetail: {
        id: "",
        userType: "",
        username: "",
        password: "",
        realName: "",
        age: "",
        unit: "",
        role: "",
        gender: 1,
        onlineType: 0,
        remark: "",
      },
      userList: [
        {
          unit: "",
          realName: "",
          username: "",
          gender: "",
          age: "",
          role: "",
          onlineType: "",
          remark: "",
        },
      ],
      selectUnit: "",
      selectRealName: "",
      totalPage: 100,
      pageNum: 1,
      pageSize: 10,
      localRealName: localStorage.getItem("realName"),
      check: localStorage.getItem("userType") === "0",
      dialogVisible: false,
      deleteId: "",
      unitList: [],
      dialogFormVisible: false,
      formLabelWidth: "120px",
      type: "",
      rules: {
        userType: [
          { required: true, message: "请输入用户类型", trigger: "change" },
        ],
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        realName: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
        ],
        age: [
          { required: true, message: "请输入年龄", trigger: "blur" },
          { min: 1, max: 2, message: "长度在 1 到 2 个字符", trigger: "blur" },
        ],
        unit: [{ required: true, message: "请输入单位", trigger: "blur" }],
        role: [{ required: true, message: "请输入角色", trigger: "blur" }],
        gender: [
          { required: true, message: "请输入用户性别", trigger: "change" },
        ],
      },
    };
  },
  mounted() {
    this.getList();
    this.getUnitList();
    window.addEventListener("beforeunload", (e) => this.loginOut());
    window.addEventListener("unload", (e) => this.loginOut());
  },
  beforeDestroy() {
    window.removeEventListener("beforeunload", (e) => this.loginOut());
    window.removeEventListener("unload", (e) => this.loginOut());
  },
  methods: {
    cellDlClick(row) {
      if(this.check === false) {
        this.dialogFormVisible = false;
        return
      }
      detail(row.id).then((res) => {
        if (res.code === 0) {
          this.userDetail = res.data;
          this.dialogFormVisible = true;
          this.type = "edit";
          console.log("111");
          console.log(this.userDetail);
        } else {
          this.$message.warning(res.msg);
        }
      });
    },
    resetForm() {
      this.pageNum = 1;
      this.pageSize = 10;
      this.selectUnit = "";
      this.selectRealName = "";
      this.getList();
    },
    loginOut() {
      let localToken = localStorage.getItem("token");
      console.log("localToken");
      console.log(localToken);
      loginOut(localToken).then((res) => {
        if (res.data === "退出成功") {
          console.log("222");
          console.log(res);
          localStorage.removeItem("token");
          this.$router.push("/");
        } else {
          this.$message.warning("退出失败");
        }
      });
    },
    getList() {
      const param = {
        unit: this.selectUnit,
        realName: this.selectRealName,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      getList(param).then((res) => {
        if (res.code === 0) {
          this.userList = res.data.list;
          this.pageNum = res.data.pageNum;
          this.pageSize = res.data.pageSize;
          this.totalPage = res.data.total;
          console.log(this.userList);
        } else if (res === "登陆过期") {
          this.$router.push("/");
        } else {
          this.$message.warning(res.msg);
        }
      });
    },
    getUnitList() {
      getUnitList().then((res) => {
        if (res.code === 0) {
          this.unitList = res.data;
        } else {
          this.$message.warning("获取失败");
        }
      });
    },
    handleDelete() {
      deleteById(this.deleteId).then((res) => {
        if (res.code === 0) {
          this.$message.success("删除成功");
          this.deleteId = "";
          this.dialogVisible = false;
          this.getList();
        } else {
          this.$message.warning("删除失败");
        }
        console.log(row);
      });
    },
    handleEdit(row) {
      detail(row.id).then((res) => {
        if (res.code === 0) {
          this.userDetail = res.data;
          this.dialogFormVisible = true;
          this.type = "edit";
          console.log("111");
          console.log(this.userDetail);
        } else {
          this.$message.warning(res.msg);
        }
      });
    },
    handleDialog(row) {
      this.deleteId = row.id;
      this.dialogVisible = true;
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getList();
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.type = "add";
    },
    submit(userDetail) {
      this.$refs.userDetailRef.validate((valid) => {
        if (valid) {
          if (this.type === "add") {
            add(this.userDetail)
              .then((res) => {
                if (res.code === 0) {
                  this.$message.success("添加成功！");
                  this.dialogFormVisible = false;
                  this.userDetail = {};
                  this.type = "";
                  this.getList();
                } else {
                  this.$message.warning(res.msg || "操作失败");
                }
              })
              .catch((err) => {
                this.$message.warning("请求出错了：" + err);
              });
          } else if (this.type === "edit") {
            update(this.userDetail)
              .then((res) => {
                if (res.code === 0) {
                  this.$message.success("修改成功！");
                  this.dialogFormVisible = false;
                  this.userDetail = {};
                  this.type = "";
                  this.getList();
                } else {
                  this.$message.warning(res.msg || "操作失败");
                }
              })
              .catch((err) => {
                this.$message.warning("请求出错了：" + err);
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.box {
  margin: 0;
  padding: 0;
  outline: 0;
}

html,
body {
  width: 100vw;
  height: 100vh;
}

.box-top {
  height: 60px;
  overflow: auto;
  background: #4099ff;
}
.left-span {
  margin: 15px;
  float: left;
}

.right-span {
  margin: 15px;
  float: right;
}

.right-button {
  margin-top: 10px;
  float: right;
}
.select {
  display: flex;
  position: relative;
  padding: 15px 0;
  /* align-items: center;
  overflow: auto; */
  /* height: 100px; */
}
.demo-input-suffix {
  display: flex;
  align-items: center;
  p {
    width: 120px;
    overflow: visible;
  }
}
.right_div {
  position: absolute;
  display: flex;
  right: 10px;
  float: right;
  .add-button {
    display: flex;
    margin-left: 10px;
    /* margin: 50px 0 10px 5px; */
    .select-button {
      height: 40px;
      margin-top: 6.5px;
    }
  }
}

.page {
  overflow: auto;
  float: right;
  margin: 30px 60px 0 0;
}
::v-deep .el-form-item__label {
  width: 120px;
}
::v-deep .el-input__inner {
  width: 90%;
}
::v-deep .el-dialog__footer{
  text-align: center;
}
</style>