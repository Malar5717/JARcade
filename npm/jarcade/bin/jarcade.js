#!/usr/bin/env node
"use strict";

// Launcher for the `jarcade` npm package. It finds the prebuilt native binary
// for the current OS/CPU in the matching platform package (installed via
// optionalDependencies) and runs it, forwarding all arguments and the TTY.
//
// The platform packages carry the actual binaries so each user downloads only
// the ~13 MB image for their own platform instead of all of them.

const { spawnSync } = require("node:child_process");
const fs = require("node:fs");
const path = require("node:path");

// Maps Node's process.platform/process.arch to the package and binary name.
const TARGETS = {
  "win32-x64": { pkg: "jarcade-win32-x64", bin: "jarcade.exe" },
  "linux-x64": { pkg: "jarcade-linux-x64", bin: "jarcade" },
  "darwin-arm64": { pkg: "jarcade-darwin-arm64", bin: "jarcade" },
};

const key = `${process.platform}-${process.arch}`;
const target = TARGETS[key];

if (!target) {
  console.error(
    `jarcade: unsupported platform "${key}".\n` +
      `Supported platforms: ${Object.keys(TARGETS).join(", ")}.`
  );
  process.exit(1);
}

let binPath;
try {
  // Resolve through the platform package's package.json so this works no matter
  // how npm hoisted it into node_modules.
  const pkgDir = path.dirname(require.resolve(`${target.pkg}/package.json`));
  binPath = path.join(pkgDir, target.bin);
} catch {
  console.error(
    `jarcade: the platform package "${target.pkg}" is not installed.\n` +
      "This can happen if optional dependencies were disabled during install.\n" +
      "Try reinstalling: npm install jarcade"
  );
  process.exit(1);
}

// Tarballs packed on Windows lose the executable bit on the POSIX binaries, so
// make sure it is set before launching (best effort).
if (process.platform !== "win32") {
  try {
    fs.chmodSync(binPath, 0o755);
  } catch {
    // Ignore; spawn below will surface a clear error if the file is unusable.
  }
}

// stdio: "inherit" keeps ANSI colours and interactive keyboard input working.
const result = spawnSync(binPath, process.argv.slice(2), { stdio: "inherit" });

if (result.error) {
  console.error(`jarcade: failed to start the binary: ${result.error.message}`);
  process.exit(1);
}

process.exit(result.status === null ? 1 : result.status);
