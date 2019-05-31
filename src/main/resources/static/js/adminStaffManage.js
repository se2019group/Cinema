$(document).ready(function () {

getUserList();

function getUserList() {
        getRequest(
            '/account/all/'
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
                                '/account/delete/'+ `?userId=${id}`,
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
function renderUserList(users) {
        $('.user-in-table').empty();
        var userDomStr="";
       users.forEach(function (user) {

                   var button1 ="<a role='button' id="+user.id+" onclick='change(this.id)'><i class='icon-edit'></i>修改员工信息</a>";
                   var button2 ="<a role='button' id="+user.id+" onclick='deleteStaff(this.id)'><i class='icon-edit'></i>删除员工</a>";
                   if(user.username!="root"){
                    var userDom =
                       "<tr>"
                        + "<td>" + user.id + "</td>"
                        + "<td>" + user.username + "</td>"
                        + "<td>" + user.password + "</td>"
                        + "<td>" + button1 + "</td>"
                        + "<td>" + button2 + "</td>"
                        + " </tr>";
                     }else{
                     var userDom =
                        "<tr>"
                        + "<td>" + user.id + "</td>"
                        + "<td>" + user.username + "</td>"
                        + "<td>" + user.password + "</td>"
                        + "<td>" + button1 + "</td>"
                        + " </tr>";
                     }
                   userDomStr+=userDom;

               });
               $('.user-in-table').append(userDomStr);
           }

function getAccount(username) {
        getRequest(
            '/search?username='+username,
            function (res) {
                renderAccount(res.content);
            },
             function (error) {
            alert(error);
        });
    }
    function renderAccount(user) {
        $('.account-on-list').empty();
            var accountDomStr = '';
            accountDomStr +=
                        "<span class='primary-text'>" + user.username + "</span>" +
                $('.account-on-list').append(accountDomStr);
    }

    $('#search-btn').click(function () {
            getAccount($('#search-input').val());
    })
});