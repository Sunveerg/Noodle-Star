import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/menu');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('felix@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('FtCJwdC7c3ZiNeD!');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await page.getByRole('button', { name: 'Manage Staff' }).click();
  const deleteButton = await page.getByRole('button', { name: 'Delete' });
  page.on('dialog', async (dialog) => {
    expect(dialog.message()).toBe('Are you sure you want to delete this staff member?');
    await dialog.accept();
  });
  await deleteButton.click();
  const successDialog = await page.locator('text=Staff member deleted successfully');
  page.on('dialog', async (dialog) => {
    expect(dialog.message()).toBe('Staff member deleted successfully');
    await dialog.accept();
  });
});
