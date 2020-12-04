import 'dart:ui';

import 'package:flustars/flustars.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:moe_protector/components/default_button.dart';
import 'package:moe_protector/util/bridge.dart';

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

mixin _LoginStateMixin<T extends StatefulWidget> on State<T> {
  int serverIndex = SpUtil.getInt("serviceIndex", defValue: 0);
  var usernameController = TextEditingController();
  var passwordController = TextEditingController();
  bool isLoginForm = true;
  var isLoading = false;

  List<dynamic> serverList = [];
  var defaultServer = "1";
  var selectHost = "";

  login() async {
    FocusScope.of(context).requestFocus(FocusNode());
    SpUtil.putInt("serviceIndex", serverIndex);
    SpUtil.putString("username", usernameController.text);
    SpUtil.putString("password", passwordController.text);

    setState(() {
      isLoading = true;
    });

    try {
      Map<String, dynamic> loginServer =
          await AndroidBridge.createBasicMessageChannel("first_login", {
        "username": usernameController.text,
        "password": passwordController.text,
        "service": serverIndex
      });

      String defaultServer = loginServer["defaultServer"];
      List<dynamic> rawServerList = loginServer["serverList"];
      setState(() {
        isLoading = false;
      });

      setState(() {
        serverList = rawServerList;
        selectHost = rawServerList
                .where((e) => e["id"] == defaultServer)
                .first["host"] ??
            "";
        isLoginForm = false;
      });
      return;
    } catch (e) {
      await showDialog(
          context: context,
          builder: (ctx) {
            return AlertDialog(
              title: Text("出错了!"),
              content: Text(e),
              actions: [
                DefaultButton(
                  child: Text("确定"),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                )
              ],
            );
          });
    }
    setState(() {
      isLoading = false;
    });
  }

  backToUser() {
    setState(() {
      isLoginForm = true;
    });
  }


}

class _LoginPageState extends State<LoginPage> with _LoginStateMixin {
  @override
  void initState() {
    super.initState();
    usernameController.text = SpUtil.getString("username");
    passwordController.text = SpUtil.getString("password");
    serverIndex = SpUtil.getInt("serverIndex");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: buildAppbar(),
      body: buildBody(context),
      extendBodyBehindAppBar: true,
    );
  }

  Widget buildBody(BuildContext context) {
    return Container(
      decoration: BoxDecoration(color: Theme.of(context).primaryColor),
      child: Center(
        child: AnimatedSwitcher(
          duration: Duration(milliseconds: 300),
          transitionBuilder: (Widget child, Animation<double> animation) {
            return ScaleTransition(child: child, scale: animation);
          },
          child:
              isLoginForm ? buildUserForm(context) : buildServerForm(context),
        ),
      ),
    );
  }

  Widget buildServerForm(BuildContext context) {
    return Container(
      width: 270,
      key: ValueKey<String>("serverForm"),
      child: Card(
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.all(Radius.circular(10))),
        child: Padding(
          padding: const EdgeInsets.fromLTRB(5, 20, 5, 10),
          child: SingleChildScrollView(
            child: Column(
              children: [
                Text(
                  "服务器",
                  style: TextStyle(
                    fontSize: 18,
                    color: Theme.of(context).primaryColor,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                SizedBox(
                  height: 5,
                )
              ]
                ..addAll(serverList.map((e) {
                  return RadioListTile(
                    title: Text(e["name"]),
                    value: e["host"],
                    groupValue: selectHost,
                    onChanged: (v) {
                      setState(() {
                        selectHost = v;
                      });
                    },
                  );
                }).toList())
                ..addAll([
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      Container(
                        width: 70,
                        child: FlatButton(
                            child: Text("取消"),
                            onPressed: backToUser,
                            shape: RoundedRectangleBorder(
                                borderRadius:
                                    BorderRadius.all(Radius.circular(3)))),
                      ),
                      SizedBox(
                        width: 3,
                      ),
                      Container(
                        width: 70,
                        child: DefaultButton(
                          child: Text("确认"),
                          onPressed: login,
                        ),
                      ),
                      SizedBox(
                        width: 5,
                      )
                    ],
                  )
                ]),
            ),
          ),
        ),
      ),
    );
  }

  Container buildUserForm(BuildContext context) {
    return Container(
      key: ValueKey<String>("userForm"),
      width: 270,
      height: 320,
      child: Card(
        elevation: 5,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(10)),
        ),
        child: Stack(
          children: [
            IgnorePointer(
              ignoring: isLoading,
              child: buildUserFormBody(context),
            ),
            Offstage(
              offstage: !isLoading,
              child: Center(
                child: BackdropFilter(
                  child: CircularProgressIndicator(),
                  filter: ImageFilter.blur(sigmaX: 3.0, sigmaY: 3.0),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  Padding buildUserFormBody(BuildContext context) {
    return Padding(
      padding: EdgeInsets.all(20),
      child: Column(
        children: [
          Text(
            "护萌宝",
            style: TextStyle(
              fontSize: 18,
              color: Theme.of(context).primaryColor,
              fontWeight: FontWeight.bold,
            ),
          ),
          Expanded(
            child: buildSign(),
          ),
          SizedBox(
            height: 25,
          ),
          FlatButton(
            onPressed: login,
            child: Text("登录"),
            minWidth: double.infinity,
            color: Theme.of(context).primaryColor,
            textColor: Colors.white,
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(3))),
          )
        ],
      ),
    );
  }

  Container buildSign() {
    return Container(
      width: double.infinity,
      height: double.infinity,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          TextField(
            decoration: InputDecoration(labelText: "用户名"),
            controller: usernameController,
          ),
          TextField(
            obscureText: true,
            decoration: InputDecoration(labelText: "密码"),
            controller: passwordController,
          ),
          SizedBox(
            height: 5,
          ),
          DropdownButton(
            isExpanded: true,
            items: [
              DropdownMenuItem(
                value: 0,
                child: Text("Android"),
              ),
              DropdownMenuItem(
                value: 1,
                child: Text("IOS"),
              ),
            ],
            onChanged: (value) {
              FocusScope.of(context).requestFocus(FocusNode());
              setState(() {
                serverIndex = value ?? 0;
              });
            },
            value: serverIndex,
          ),
        ],
      ),
    );
  }

  AppBar buildAppbar() {
    return AppBar(
      elevation: 0,
      backgroundColor: Colors.transparent,
    );
  }
}
