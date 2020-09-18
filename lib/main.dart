import 'package:flutter/material.dart';
import 'package:moviedb/ui/home/home.dart';
import 'package:moviedb/ui/theme.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: MovieDBTheme,
      home: HomePage(),
    );
  }
}
