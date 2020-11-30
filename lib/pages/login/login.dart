import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class LoginPage extends StatefulWidget {
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  String username = "";
  String password = "";
  int serverIndex = 0;

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
        child: Container(
          width: 270,
          height: 320,
          child: Card(
            elevation: 5,
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(10))),
            child: buildCardBody(context),
          ),
        ),
      ),
    );
  }

  Padding buildCardBody(BuildContext context) {
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
            child: buildSignForm(),
          ),
          SizedBox(
            height: 25,
          ),
          FlatButton(
            onPressed: () {},
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

  Container buildSignForm() {
    return Container(
      width: double.infinity,
      height: double.infinity,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          TextField(
            decoration: InputDecoration(labelText: "用户名"),
            onChanged: (data) {
              setState(() {
                username = data;
              });
            },
          ),
          TextField(
            obscureText: true,
            decoration: InputDecoration(labelText: "密码"),
            onChanged: (data) {
              setState(() {
                password = data;
              });
            },
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
            onChanged: (int? value) {
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
