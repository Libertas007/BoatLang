import 'dart:io';

import 'lexer.dart';
import 'scheduler.dart';
import 'context.dart';
import 'variables.dart';
import 'registry.dart';
import 'libraryloader.dart' as lib;
import 'package:path/path.dart' as p;
import 'package:http/http.dart' as http;
import 'dart:convert';

final String version = "v1.3";
final Registry registry = Registry();

int lineNum = -1;
bool started = false;
Scheduler scheduler = Scheduler([]);
List<CodeBlockData> blocks = [];
ExecutionContext globalContext = ExecutionContext();
final String TEMPLATE =
    "SAIL ON yacht\n\n// Here comes your code\n// Remove comment below to see \"Hello world!\" program\n// BROADCAST \"Hello world!\"\n\nARRIVE AT port";

void boatMainEntryPoint(List<String> args) async {
  lib.load();
  globalContext.variablePool.createVariable("YES", Switch(true));
  globalContext.variablePool.createVariable("NO", Switch(false));
  globalContext.variablePool.variables["YES"]!.applyModifier(Constant());
  globalContext.variablePool.variables["NO"]!.applyModifier(Constant());

  final Map<String, String> packageNames = {
    "windows": "boat-update-windows.exe",
    "linux": "boat-update-linux",
    "macos": "boat-update-mac",
  };

  if (File(boatHomeDirectory() + p.separator + ".config").readAsStringSync() ==
      "afterupdate=1") {
    print("Performing after-update jobs...");

    String res = (await http.get(Uri.parse(
            "https://api.github.com/repos/Libertas007/BoatLang/releases/latest")))
        .body;

    Map<String, dynamic> asMap = jsonDecode(res);

    List<dynamic> assets = asMap["assets"];

    String downloadUrl = assets.firstWhere((asset) =>
        asset["name"] ==
        packageNames[Platform.operatingSystem])["browser_download_url"];

    final newVersion = await http.get(Uri.parse(downloadUrl));

    final file = File(pathToBoatUpdater());

    file.writeAsBytesSync(newVersion.bodyBytes);
    File(boatHomeDirectory() + p.separator + ".config")
        .writeAsStringSync("afterupdate=0");

    print("Done");
  }

  if (args.isEmpty) {
    print("""          ooooo
       _ooo
       H
|======H========|
\\     BOAT     /
 \\____________/
~~~~~~~~~~~~~~~~~""");
    print("Using version '$version'");
    print("Welcome to terminal for Boat! Start typing some Boat commands!");
    while (true) {
      started = true;
      stdout.write("Boat terminal > ");
      String? input = stdin.readLineSync();

      input ??= "";
      if (input == "") continue;
      final Lexer lexer = Lexer(input);
      final Scheduler scheduler = Scheduler(lexer.tokens);

      print("-> " +
          executeExpression(scheduler.tasks[0], globalContext).toString());
    }
  } else if (args[0] == "new") {
    createNew(args);
  } else {
    String file =
        readFileSync(args[0].endsWith(".boat") ? args[0] : args[0] + ".boat");

    Lexer lexer = Lexer(file);

    scheduler = Scheduler(lexer.tokens);

    for (globalContext.taskPointer = 0;
        globalContext.taskPointer < scheduler.tasks.length;
        globalContext.taskPointer++) {
      final task = scheduler.tasks[globalContext.taskPointer];
      lineNum = task.lineNumber;
      if (task.tokens.isNotEmpty) {
        executeExpression(task, globalContext);
      }
    }
  }

  exit(0);
}

void createNew(List<String> args) {
  print("Creating new project from default template...");
  final String fileName = args[1];
  final File file =
      File(fileName.endsWith(".boat") ? fileName : fileName + ".boat");

  if (file.existsSync()) {
    stdout.write(
        "File '${fileName.endsWith(".boat") ? fileName : fileName + ".boat"}' already exists. Running 'boat new' will overwrite it. Do you want to continue? (y/N) ");
    String answer = stdin.readLineSync() ?? "n";
    if (answer.toLowerCase() != "y") {
      print("Aborting action...");
      return;
    }
  }
  file.createSync();
  file.writeAsStringSync(TEMPLATE);
  print(
      "Created new file '${fileName.endsWith(".boat") ? fileName : fileName + ".boat"}'");
}

Variable executeExpression(Task task, ExecutionContext ctx) {
  final String command = task.tokens.first.commandName ??
      task.tokens.first.keyword ??
      task.tokens.first.variableName ??
      "";

  if (task.tokens.first.type == TokenType.BlockEnd) {
    return executeEnd(task, ctx);
  }

  if (task.tokens.first.commandName == "SAIL ON") {
    return executeSail(task, ctx);
  }

  if (started)
    switch (command) {
      case "ADD":
        return executeAdd(task, ctx);
      case "ARRIVE AT":
        return executeArrive(task, ctx);
      case "BROADCAST":
        return executeBroadcast(task, ctx);
      case "CRASH INTO":
        return executeCrash(task, ctx);
      case "DIVIDE":
        return executeDivide(task, ctx);
      case "DROP":
        return executeDrop(task, ctx);
      case "IF":
        return executeIf(task, ctx);
      case "LISTEN TO":
        return executeListen(task, ctx);
      case "LOOP":
        return executeLoop(task, ctx);
      case "MULTIPLY":
        return executeMultiply(task, ctx);
      case "REPACK":
        return executeRepack(task, ctx);
      case "REQUEST":
        return executeRequest(task, ctx);
      case "RETURN":
        return executeReturn(task, ctx);
      case "SET":
        return executeSet(task, ctx);
      case "SINK":
        return executeSink(task, ctx);
      case "SUBTRACT":
        return executeSubtract(task, ctx);
      case "WAIT":
        return executeWait(task, ctx);
      default:
        if (ctx.variablePool.functionExists(command)) {
          List<Token> args = task.tokens;
          args.removeAt(0);
          return ctx.variablePool.getFunction(command).run(args);
        }
        return error("Unknown command");
    }
  return error("'SAIL ON' must be the first command!");
}

// Syntax: ADD [BARREL] TO [BARREL]
Variable executeAdd(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 4 || task.tokens[2].keyword != "TO")
    error("Hey captain, we don't understand your orders!");

  double toAdd = 0;
  ArgumentHelper toAddArg = ArgumentHelper(task.tokens[1], ctx);

  if (toAddArg.variable is Barrel) {
    toAdd = toAddArg.variable.value;
  } else {
    error("We cannot add it!");
  }

  if (task.tokens.last.type != TokenType.VariableName) {
    error("Hey captain, we don't understand your orders!");
  }

  ctx.variablePool.setVariable(
      task.tokens.last.variableName ?? "",
      Barrel(toAdd +
          ctx.variablePool
              .getVariable(task.tokens.last.variableName ?? "")
              .value));

  return ctx.variablePool.getVariable(task.tokens.last.variableName ?? "");
}

// Syntax: ARRIVE AT [PACKAGE]
Variable executeArrive(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 2) {
    return error("Hey captain, we don't understand your orders!!");
  }

  return exit(0);
}

// Syntax: BROADCAST [BARREL|PACKAGE]
Variable executeBroadcast(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 2) {
    return error("Hey captain, we don't understand your orders!");
  }

  ArgumentHelper toPrintArg = ArgumentHelper(task.tokens[1], ctx);

  print(toPrintArg.variable);
  return toPrintArg.variable;
}

// Syntax: CRASH INTO [PACKAGE]
Variable executeCrash(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 2) {
    return error("Hey captain, we don't understand your orders!");
  }

  return exit(1);
}

// Syntax: DIVIDE [BARREL] BY [BARREL]
Variable executeDivide(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 4 || task.tokens[2].keyword != "BY") {
    return error("Hey captain, we don't understand your orders!");
  }

  double toDivide = 0;
  ArgumentHelper toDivideArg = ArgumentHelper(task.tokens.last, ctx);

  if (toDivideArg is Barrel) {
    toDivide = toDivideArg.variable.value;
  } else {
    error("We cannot divide it!");
  }

  if (task.tokens[1].type != TokenType.VariableName) {
    error("Hey captain, we don't understand your orders!");
  }

  if (toDivide == 0) {
    error("Hey captain, we cannot divide by zero!");
  }

  ctx.variablePool.setVariable(
      task.tokens.last.variableName ?? "",
      Barrel(ctx.variablePool
              .getVariable(task.tokens.last.variableName ?? "")
              .value /
          toDivide));

  return ctx.variablePool.getVariable(task.tokens.last.variableName ?? "");
}

// Syntax: DROP [BARREL|PACKAGE]
Variable executeDrop(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 2) {
    return error("Hey captain, we don't understand your orders!");
  }

  if (ctx.variablePool.variables.containsKey(task.tokens.last.variableName)) {
    ctx.variablePool.variables.remove(task.tokens.last.variableName);
  }

  return None();
}

// Syntax: END
Variable executeEnd(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 1) {
    return error("Hey captain, we don't understand your orders!!");
  }

  CodeBlockData last = blocks.last;

  if (last.type == CodeBlockType.If) {
    blocks.removeLast();
    return None();
  }

  if (last.type == CodeBlockType.Loop) {
    if (last.iterationsLeft == 0) {
      blocks.removeLast();
      return None();
    }

    blocks[blocks.length - 1].iterationsLeft--;
    ctx.taskPointer = last.start;
    if (last.iterationVar != "")
      ctx.variablePool.setVariable(last.iterationVar,
          Barrel(ctx.variablePool.getVariable(last.iterationVar).value + 1));

    return None();
  }

  return None();
}

// Syntax: LISTEN TO [PACKAGE]
Variable executeListen(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 2) {
    return error("Hey captain, we don't understand your orders!");
  }

  if (ctx.variablePool.getVariable(task.tokens.last.variableName ?? "")
      is Package) {
    return error("'${task.tokens.last.variableName}' is not a PACKAGE");
  }

  String input = stdin.readLineSync() ?? "";

  ctx.variablePool
      .setVariable(task.tokens.last.variableName ?? "", Package(input));

  return Package(input);
}

// Syntax: LOOP [BARREL] TIMES: ... END
Variable executeLoop(Task task, ExecutionContext ctx) {
  if (task.tokens.length > 5 ||
      task.tokens[2].keyword != "TIMES" ||
      task.tokens.last.type != TokenType.BlockStart) {
    return error("Hey captain, we don't understand your orders!");
  }

  double iterations = 0;
  ArgumentHelper iterationsArg = ArgumentHelper(task.tokens[1], ctx);
  if (iterationsArg.variable is Barrel) {
    iterations = iterationsArg.variable.value;
  } else {
    error("Cannot loop!");
  }

  CodeBlockData toAdd = CodeBlockData(
    start: ctx.taskPointer,
    type: CodeBlockType.Loop,
    iterationsLeft: iterations.ceil() - 1,
  );

  if (task.tokens.length == 5 && task.tokens[3].keyword == "AS") {
    ctx.variablePool
        .createVariable(task.tokens[4].variableName ?? "", Barrel(0));
    toAdd.iterationVar = task.tokens[4].variableName ?? "";
  }

  blocks.add(toAdd);
  return None();
}

// Syntax: IF [BARREl|PACKAGE] [OPERATOR] [BARREL|PACKAGE]: ... END
Variable executeIf(Task task, ExecutionContext ctx) {
  if (task.tokens.last.type != TokenType.BlockStart ||
      task.tokens.length != 5) {
    error("Hey captain, we don't understand your orders!");
  }

  ArgumentHelper left = ArgumentHelper(task.tokens[1], ctx);
  OperatorType op = task.tokens[2].operatorType ?? OperatorType.Not;
  ArgumentHelper right = ArgumentHelper(task.tokens[3], ctx);
  bool valid = evaluateCondition(left, op, right);

  blocks.add(CodeBlockData(start: ctx.taskPointer, type: CodeBlockType.If));
  int nesting = 0;
  while (!valid) {
    ctx.taskPointer++;

    if (ctx.taskPointer == scheduler.tasks.length) break;

    if (blockKeywords.contains(
        scheduler.tasks[ctx.taskPointer].tokens.first.keyword)) nesting++;

    if (scheduler.tasks[ctx.taskPointer].tokens.first.type ==
        TokenType.BlockEnd) {
      if (nesting == 0) break;

      nesting--;
    }
  }
  return None();
}

// Syntax: MULTIPLY [BARREL] BY [BARREL]
Variable executeMultiply(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 4 || task.tokens[2].keyword != "BY") {
    return error("Hey captain, we don't understand your orders!");
  }

  double toMultiply = 0;
  ArgumentHelper toMultiplyArg = ArgumentHelper(task.tokens.last, ctx);

  if (toMultiplyArg.variable is Barrel) {
    toMultiply = toMultiplyArg.variable.value;
  } else {
    error("Cannot multiply!");
  }

  if (task.tokens[1].type != TokenType.VariableName) {
    error("Hey captain, we don't understand your orders!");
  }

  ctx.variablePool.setVariable(
      task.tokens.last.variableName ?? "",
      Barrel(ctx.variablePool
              .getVariable(task.tokens.last.variableName ?? "")
              .value *
          toMultiply));

  return ctx.variablePool.getVariable(task.tokens[1].variableName ?? "");
}

// Syntax: REPACK [BARREL|PACKAGE] TO (BARREL|PACKAGE)
Variable executeRepack(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 4 || task.tokens[2].keyword != "TO") {
    return error("Hey captain, we don't understand your orders!");
  }

  if (!ctx.variablePool.variables.containsKey(task.tokens[1].variableName))
    error("Variable doesn't exist!");

  if (ctx.variablePool.getVariable(task.tokens[1].variableName ?? "")
      is Barrel) {
    if (task.tokens[3].variableType == "PACKAGE") {
      String value = ctx.variablePool
          .getVariable(task.tokens[1].variableName ?? "")
          .toString();

      ctx.variablePool
          .setVariable(task.tokens[1].variableName ?? "", Package(value));
      return None();
    }
    return None();
  }

  if (ctx.variablePool.getVariable(task.tokens[1].variableName ?? "")
      is Package) {
    if (task.tokens[3].variableType == "BARREL") {
      double? value = double.tryParse(ctx.variablePool
              .getVariable(task.tokens[1].variableName ?? "")
              .value ??
          "");

      if (value == null) {
        error(
            "Hey, this package ${task.tokens[1].variableName} cannot be converted to BARREL!");
        return None();
      }

      ctx.variablePool
          .setVariable(task.tokens[1].variableName ?? "", Barrel(value));
      return None();
    }
    return None();
  }

  return None();
}

// Syntax: REQUEST (BARREL|PACKAGE) [PACKAGE]
Variable executeRequest(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 3 ||
      task.tokens[1].type != TokenType.VariableType) {
    return error("Hey captain, we don't understand your orders!");
  }

  Variable toCreate = registry.variableTypes
      .firstWhere((element) => element.typeName == task.tokens[1].variableType);

  ctx.variablePool.createVariable(task.tokens[2].variableName ?? "", toCreate);

  error("Hey captain, we don't understand your orders!");
  return None();
}

// Syntax: RETURN [BARREL|PACKAGE]
Variable executeReturn(Task task, ExecutionContext ctx) {
  return executeDrop(task, ctx);
}

// Syntax: SAIL ON [PACKAGE]
Variable executeSail(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 2) {
    return error("Hey captain, we don't understand your orders!");
  }
  started = true;
  return None();
}

// Syntax: SET [BARREL|PACKAGE] TO [BARREL|PACKAGE]
Variable executeSet(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 4 || task.tokens[2].keyword != "TO") {
    return error("Hey captain, we don't understand your orders!");
  }

  ArgumentHelper toSetArg = ArgumentHelper(task.tokens.last, ctx);

  if (!toSetArg.variable.sameType(
      ctx.variablePool.getVariable(task.tokens[1].variableName ?? ""))) {
    error(
        "Cannot set ${task.tokens[1].variableName} to ${toSetArg.variable} because they are made of different types!");
  }

  ctx.variablePool
      .setVariable(task.tokens[1].variableName ?? "", toSetArg.variable);

  return ctx.variablePool.getVariable(task.tokens[1].variableName ?? "");
}

// Syntax: SINK
Variable executeSink(Task task, ExecutionContext ctx) {
  return exit(1);
}

// Syntax: SUBTRACT [BARREL] FROM [BARREL]
Variable executeSubtract(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 4 || task.tokens[2].keyword != "FROM") {
    error("Hey captain, we don't understand your orders!");
  }

  double toSubtract = task.tokens[1].barrelValue ??
      ctx.variablePool.getVariable(task.tokens[1].variableName ?? "").value;

  ArgumentHelper toSubtractArg = ArgumentHelper(task.tokens[1], ctx);

  if (toSubtractArg.variable is Barrel) {
    toSubtract = toSubtractArg.variable.value;
  } else {
    error("We cannot subtract it!");
  }

  ctx.variablePool.setVariable(
      task.tokens.last.variableName ?? "",
      Barrel(ctx.variablePool
              .getVariable(task.tokens.last.variableName ?? "")
              .value -
          toSubtract));

  return ctx.variablePool.getVariable(task.tokens.last.variableName ?? "");
}

// Syntax: WAIT [BARREL] (in ms)
Variable executeWait(Task task, ExecutionContext ctx) {
  if (task.tokens.length != 2) {
    return error("Hey captain, we don't understand your orders!");
  }

  double ms = 0;

  ArgumentHelper msArg = ArgumentHelper(task.tokens.last, ctx);

  if (msArg.variable is Barrel) {
    ms = msArg.variable.value;
  } else {
    error("Cannot wait!");
  }

  ms = ms.roundToDouble();

  sleep(Duration(milliseconds: ms.toInt()));
  return Barrel(ms);
}

Variable error(String text) {
  print("$text (line $lineNum)");
  exit(1);
}

String readFileSync(String file) {
  return new File(file).readAsStringSync();
}

bool evaluateCondition(
    ArgumentHelper left, OperatorType op, ArgumentHelper right) {
  if ([OperatorType.Equals, OperatorType.NotEquals].contains(op)) {
    if (left.variable is Barrel && right.variable is Barrel) {
      bool equal = left.variable.value == right.variable.value;

      if (op == OperatorType.Equals) {
        return equal;
      } else
        return !equal;
    }

    if (left.variable is Package && right.variable is Package) {
      bool equal = left.variable.value == right.variable.value;

      if (op == OperatorType.Equals) {
        return equal;
      } else
        return !equal;
    }

    error(
        "Cannot compare ${left.variable is Barrel ? "BARREL" : "PACKAGE"} to ${right.variable is Barrel ? "BARREL" : "PACKAGE"}");
  }

  if (left.variable is Barrel || right.variable is Barrel) {
    error("Cannot use PACKAGE values in numeric conditions");
  }

  switch (op) {
    case OperatorType.GraterOrEqual:
      return left.variable.value >= right.variable.value;
    case OperatorType.GraterThan:
      return left.variable.value > right.variable.value;
    case OperatorType.LessOrEqual:
      return left.variable.value <= right.variable.value;
    case OperatorType.LessThan:
      return left.variable.value < right.variable.value;
    default:
  }
  return false;
}

class CodeBlockData {
  int start;
  CodeBlockType type;
  int iterationsLeft;
  String iterationVar;

  CodeBlockData({
    required this.start,
    required this.type,
    this.iterationsLeft = 0,
    this.iterationVar = "",
  });
}

enum CodeBlockType {
  Loop,
  If,
  Else,
  While,
}

String homeDirectory() {
  switch (Platform.operatingSystem) {
    case 'linux':
    case 'macos':
      return Platform.environment['HOME'] ?? "";
    case 'windows':
      return Platform.environment['USERPROFILE'] ?? "";
  }
  return "";
}

String boatHomeDirectory() {
  return homeDirectory() + p.separator + ".boat";
}

String boatBinDirectory() {
  return boatHomeDirectory() + p.separator + "bin";
}

String pathToBoatExecutable() {
  return boatBinDirectory() +
      p.separator +
      "boat" +
      (Platform.isWindows ? ".exe" : "");
}

String pathToBoatUpdater() {
  return boatBinDirectory() +
      p.separator +
      "update" +
      (Platform.isWindows ? ".exe" : "");
}
