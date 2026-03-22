#!/usr/bin/env node

/**
 * Todo Persistence Utility
 * 
 * Saves and loads TodoWrite state to/from a JSON file.
 * Used to persist plan execution progress across sessions.
 * 
 * Usage:
 *   node scripts/todo-persist.js save <file.json> "<title>" <todos>
 *   node scripts/todo-persist.js load <file.json>
 * 
 * Example:
 *   node scripts/todo-persist.js save plan-progress.json "Feature Implementation" '[{"content":"Task 1","status":"completed"},{"content":"Task 2","status":"in_progress"}]'
 */

const fs = require('fs');
const path = require('path');

const COMMANDS = {
  SAVE: 'save',
  LOAD: 'load',
  UPDATE: 'update'
};

function saveTodos(filePath, title, todos) {
  const data = {
    title,
    todos: typeof todos === 'string' ? JSON.parse(todos) : todos,
    updatedAt: new Date().toISOString()
  };
  
  fs.writeFileSync(filePath, JSON.stringify(data, null, 2));
  console.log(JSON.stringify(data, null, 2));
}

function loadTodos(filePath) {
  if (!fs.existsSync(filePath)) {
    console.log(JSON.stringify({ title: '', todos: [], updatedAt: null }));
    return;
  }
  
  const content = fs.readFileSync(filePath, 'utf8');
  console.log(content);
}

function updateTodo(filePath, taskIndex, newStatus) {
  if (!fs.existsSync(filePath)) {
    console.error('No progress file found');
    process.exit(1);
  }
  
  const content = fs.readFileSync(filePath, 'utf8');
  const data = JSON.parse(content);
  
  if (taskIndex >= 0 && taskIndex < data.todos.length) {
    data.todos[taskIndex].status = newStatus;
    data.updatedAt = new Date().toISOString();
    fs.writeFileSync(filePath, JSON.stringify(data, null, 2));
  }
  
  console.log(JSON.stringify(data, null, 2));
}

function printProgress(data) {
  if (!data || !data.todos) {
    console.log('No progress data found');
    return;
  }
  
  const total = data.todos.length;
  const completed = data.todos.filter(t => t.status === 'completed').length;
  const inProgress = data.todos.filter(t => t.status === 'in_progress').length;
  const pending = data.todos.filter(t => t.status === 'pending').length;
  
  console.log(`\n📋 ${data.title || 'Plan Progress'}`);
  console.log(`   Last updated: ${data.updatedAt || 'Never'}`);
  console.log(`   Progress: ${completed}/${total} completed`);
  console.log(`   Status: ${inProgress} in progress, ${pending} pending`);
  console.log('\n   Tasks:');
  
  data.todos.forEach((todo, i) => {
    const icon = todo.status === 'completed' ? '✅' : todo.status === 'in_progress' ? '🔄' : '⏳';
    const priority = todo.priority ? ` [${todo.priority.toUpperCase()}]` : '';
    console.log(`   ${icon} ${i + 1}. ${todo.content}${priority} (${todo.status})`);
  });
  
  console.log('');
}

function main() {
  const args = process.argv.slice(2);
  const command = args[0];
  
  if (!command) {
    console.error('Usage: node todo-persist.js <save|load|update> [options]');
    console.error('  save <file> <title> <todos-json>');
    console.error('  load <file>');
    console.error('  update <file> <task-index> <status>');
    process.exit(1);
  }
  
  switch (command) {
    case COMMANDS.SAVE:
      saveTodos(args[1], args[2], args[3]);
      break;
    case COMMANDS.LOAD:
      const data = loadTodos(args[1]);
      printProgress(data ? JSON.parse(data) : null);
      break;
    case COMMANDS.UPDATE:
      updateTodo(args[1], parseInt(args[2]), args[3]);
      break;
    default:
      console.error(`Unknown command: ${command}`);
      process.exit(1);
  }
}

main();
