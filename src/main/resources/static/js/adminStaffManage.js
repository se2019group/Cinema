$(document).ready(function () {

getUserList();

function getUserList() {
        getRequest(
            '/cinemaMember/all/'
            ,
            function (res) {
                renderUserList(res.content);
            },
            function (error) {
                alert(error);
            });
    }
    deleteStaff=function(id){
        var r=confirm("请再次确认")
        if (r==true){
             postRequest(
                                '/cinemaMember/delete/'+ `?userId=${id}`,
                                {},
                                function (res) {
                                    if (res.success) {
                                        window.location.reload()
                                    } else {
                                        alert(res.message)
                                    }
                                },
                                function (error) {
                                    alert(JSON.stringify(error));
                                }

                             );
        }
        else
            return;

    }
    $("#staff-form-btn").click(function () {
                var CinemaMemberForm = getStaffForm();
                if(!validateStaffForm(CinemaMemberForm)) {
                    return;
                }
                postRequest(
                    '/cinemaMember/add/',
                    CinemaMemberForm,
                    function (res) {
                        $("#staffModal").modal('hide');
                        window.location.reload();
                    },
                     function (error) {
                        alert(error);
                    });
            });
            function validateStaffForm(data) {
                        var isValidate = true;
                        if(!data.type) {
                            isValidate = false;
                            alert("员工类型不能为空")
                            return isValidate;
                        }
                        if(!data.username) {
                            isValidate = false;
                            alert("员工姓名不能为空")
                            return isValidate;
                        }
                        if(!data.password) {
                            isValidate = false;
                            alert("员工密码不能为空")
                        }
                        return isValidate;
                    }

            function getStaffForm() {
                        return {
                            type: $('#staff-type-input').val(),
                            username: $('#staff-name-input').val(),
                            password: $('#staff-password-input').val(),
                        };
                }
function renderUserList(users) {
        $('.user-in-table').empty();
        var userDomStr="";
       users.forEach(function (user) {

                   var button1 ="<a role='button' id="+user.id+" onclick='change(this.id)'><i class='icon-edit'></i>修改员工信息</a>";
                   var button2 ="<a role='button' id="+user.id+" onclick='deleteStaff(this.id)'><i class='icon-edit'></i>删除员工</a>";

                   if(user.type==0){
                    var userDom =
                       "<tr>"
                        + "<td>" + "排片经理" + "</td>"
                        + "<td>" + user.id + "</td>"
                        + "<td>" + user.username + "</td>"
                        + "<td>" + user.password + "</td>"
                        + "<td>" + button1 + "</td>"
                        + "<td>" + button2 + "</td>"
                        + " </tr>";
                        }else if(user.type==1){
                        var userDom =
                            "<tr>"
                                + "<td>" + "用户经理" + "</td>"
                                + "<td>" + user.id + "</td>"
                                + "<td>" + user.username + "</td>"
                                + "<td>" + user.password + "</td>"
                                + "<td>" + button1 + "</td>"
                                + "<td>" + button2 + "</td>"
                                + " </tr>";
                        }else{
                        var userDom =
                                "<tr>"
                                + "<td>" + "闲杂人员" + "</td>"
                                + "<td>" + user.id + "</td>"
                                + "<td>" + user.username + "</td>"
                                + "<td>" + user.password + "</td>"
                                + "<td>" + button1 + "</td>"
                                + "<td>" + button2 + "</td>"
                                + " </tr>";
                        }


                   userDomStr+=userDom;

               });
               $('.user-in-table').append(userDomStr);
           }

            change=function (id) {
               userId=id;
               $('#staffchange').modal();

               };
               $("#staff-form-btn2").click(function () {
                           var username=$("#staff-name-input2").val();
                                  var type=$("#staff-type-input2").val();
                                  var password=$("#staff-password-input2").val();
                                  var cinemaMemberForm={
                                      type:type,
                                      id: userId,
                                      username: username,
                                      password: password,
                                  }
                                  if(!validateStaffForm(cinemaMemberForm)) {
                                      return;
                                  }
                                      postRequest(
                                          '/cinemaMember/update',
                                              cinemaMemberForm,
                                          function (res) {
                                           $("#staffchange").modal('hide');
                                            window.location.reload();
                                            getUserList();
                                          },
                                          function (error) {
                                                  alert(error);
                                              });
                       });

function getAccount(username) {
        getRequest(
            '/search/member?username='+username,
            function (res) {
            if(res.content!=null){
            renderAccount(res.content);}
            },
             function (error) {
            alert(error);
        });
    }
    function renderAccount(users) {
          $('.user-in-table').empty();
                var userDomStr="";
               users.forEach(function (user) {

                           var button1 ="<a role='button' id="+user.id+" onclick='change(this.id)'><i class='icon-edit'></i>修改员工信息</a>";
                           var button2 ="<a role='button' id="+user.id+" onclick='deleteStaff(this.id)'><i class='icon-edit'></i>删除员工</a>";

                           if(user.type==0){
                            var userDom =
                               "<tr>"
                                + "<td>" + "排片经理" + "</td>"
                                + "<td>" + user.id + "</td>"
                                + "<td>" + user.username + "</td>"
                                + "<td>" + user.password + "</td>"
                                + "<td>" + button1 + "</td>"
                                + "<td>" + button2 + "</td>"
                                + " </tr>";
                                }else if(user.type==1){
                                var userDom =
                                    "<tr>"
                                        + "<td>" + "用户经理" + "</td>"
                                        + "<td>" + user.id + "</td>"
                                        + "<td>" + user.username + "</td>"
                                        + "<td>" + user.password + "</td>"
                                        + "<td>" + button1 + "</td>"
                                        + "<td>" + button2 + "</td>"
                                        + " </tr>";
                                }else{
                                var userDom =
                                        "<tr>"
                                        + "<td>" + "闲杂人员" + "</td>"
                                        + "<td>" + user.id + "</td>"
                                        + "<td>" + user.username + "</td>"
                                        + "<td>" + user.password + "</td>"
                                        + "<td>" + button1 + "</td>"
                                        + "<td>" + button2 + "</td>"
                                        + " </tr>";
                                }


                           userDomStr+=userDom;

                       });
                       $('.user-in-table').append(userDomStr);
    }

    $('#search-btn').click(function () {
            getAccount($('#search-input').val());
    })
});