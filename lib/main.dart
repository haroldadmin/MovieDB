import 'package:flutter/material.dart';
import 'package:moviedb/app/config_reader.dart';
import 'package:moviedb/ui/home/home.dart';
import 'package:moviedb/ui/theme.dart';

import 'app/config.dart';

void main() async {
  final config = await ConfigReader().read();
  runApp(MyApp(config));
}

class MyApp extends StatelessWidget {
  final Config config;

  MyApp(this.config);

  @override
  Widget build(BuildContext context) {
    print("API Key: ${config.apiKey}");
    return MaterialApp(
      title: 'Flutter Demo',
      theme: MovieDBTheme,
      home: HomePage(),
    );
  }
}
