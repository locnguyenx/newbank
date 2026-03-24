import { describe, it, expect } from 'vitest';
import { authService } from './authService';

describe('authService', () => {
  it('should be defined', () => {
    expect(authService).toBeDefined();
  });

  it('should have login method', () => {
    expect(typeof authService.login).toBe('function');
  });

  it('should have refresh method', () => {
    expect(typeof authService.refresh).toBe('function');
  });

  it('should have logout method', () => {
    expect(typeof authService.logout).toBe('function');
  });

  it('should have enrollMfa method', () => {
    expect(typeof authService.enrollMfa).toBe('function');
  });

  it('should have verifyMfa method', () => {
    expect(typeof authService.verifyMfa).toBe('function');
  });
});