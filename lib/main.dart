import 'package:flustars/flustars.dart';
import 'package:flutter/material.dart';
import 'package:moe_protector/pages/login/login.dart';
import 'package:moe_protector/themes.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SpUtil.getInstance();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '护萌宝',
      debugShowCheckedModeBanner: false,
      theme: pinkTheme,
      home: LoginPage(),
    );
  }
}