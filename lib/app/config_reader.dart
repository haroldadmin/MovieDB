import 'dart:convert';

import 'package:flutter/services.dart';

import 'config.dart';

class ConfigReader {
  final String _configFilePath;

  ConfigReader({String configPath = "/config/config.json"})
      : _configFilePath = configPath;

  Config readFrom(String src) {
    final configJson = jsonDecode(src);
    return Config.fromJson(configJson);
  }

  Future<Config> read() async {
    final configString = await rootBundle.loadString(_configFilePath);
    return Config.fromJson(jsonDecode(configString));
  }
}
