import 'package:flutter/services.dart';
import 'dart:convert';

class AndroidBridge {
  static final String channelId = "ink.z31.moe_protector";

  static Future<dynamic> createBasicMessageChannel<T>(
      String method, T json) async {
    var channel = BasicMessageChannel("$channelId/$method", StringCodec());
    var androidReply = await channel.send(jsonEncode(json));
    Map<String, dynamic> reply = jsonDecode(androidReply);
    if (reply["code"] != 0) {
      throw reply["errMsg"] as String;
    }
    return reply["data"];
  }
}
