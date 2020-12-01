import 'package:flutter/material.dart';

class DefaultButton extends StatelessWidget {
  final Function onPressed;
  final Widget child;

  const DefaultButton({Key key, this.onPressed, this.child}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return FlatButton(
      child: child,
      onPressed: onPressed,
      color: Theme.of(context).primaryColor,
      textColor: Colors.white,
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(3))),
    );
  }
}
