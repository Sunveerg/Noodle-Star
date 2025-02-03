import { test, expect } from '@playwright/test';

test('financialReportTest', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').fill('zako@gmail.com');
  await page.getByLabel('Email address').press('Tab');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('R');
  await page.getByLabel('Password').press('CapsLock');
  await page.getByLabel('Password').fill('Ronaldo07');
  await page.getByLabel('Password').press('Enter');
  await page.getByRole('button', { name: 'Financial Report' }).click();
  await page.getByRole('button', { name: 'Generate Report' }).click();
  await expect(page.getByText('Report Type: Financial Report')).toBeVisible();
  await page
    .locator('#webpack-dev-server-client-overlay')
    .contentFrame()
    .getByText('Uncaught runtime errors:×')
    .click();
  await page
    .locator('#webpack-dev-server-client-overlay')
    .contentFrame()
    .getByLabel('Dismiss')
    .click();
  await expect(page.getByText('Report Name: Total Revenue')).toBeVisible();
  await expect(page.getByText('$$$: 799')).toBeVisible();
});
