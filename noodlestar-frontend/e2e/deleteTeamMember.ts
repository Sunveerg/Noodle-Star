import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/aboutus');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByRole('textbox', { name: 'Email address' }).click();
  await page.getByRole('textbox', { name: 'Email address' }).fill('felix@gmail.com');
  await page.getByRole('textbox', { name: 'Password' }).click();
  await page.getByRole('textbox', { name: 'Password' }).fill('FtCJwdC7c3ZiNeD!');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await page.getByRole('menuitem', { name: 'About Us' }).click();
  const deleteButton = page.getByRole('button', { name: 'Delete' }).first();
  await expect(deleteButton).toBeVisible();
  await page.locator('#webpack-dev-server-client-overlay').contentFrame().getByRole('button', { name: 'Dismiss' }).click();
  page.on('dialog', async (dialog) => {
    expect(dialog.message()).toBe('Are you sure you want to delete this team member?');
    await dialog.accept();
  });
  await deleteButton.click();
  await expect(page.locator('.teamMember').first()).not.toBeVisible();
});