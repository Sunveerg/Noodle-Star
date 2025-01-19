import { test, expect } from '@playwright/test';

test('addStaffRole', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('jshn2004@hotmail.com');
  await page.getByLabel('Email address').press('Tab');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('C');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('C');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('Computer');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('ComputerS');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('ComputerScience123#');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await page.getByRole('button', { name: 'Manage Staff' }).click();
  await page.getByRole('button', { name: 'Add Staff' }).click();
  await page.locator('div:nth-child(11) > .user-actions > .btn-add-staff').click();
  await expect(page.getByText('User has been successfully granted the Staff role.')).toBeVisible();
  await page.getByRole('menuitem', { name: 'Account' }).click();
  await page.getByRole('button', { name: 'Manage Staff' }).click();
});