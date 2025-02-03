import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/home');
  await page.getByRole('button', { name: 'Login' }).click();
  await page.getByLabel('Email address').click();
  await page.getByLabel('Email address').fill('felix@gmail.com');
  await page.getByLabel('Password').click();
  await page.getByLabel('Password').fill('FtCJwdC7c3ZiNeD!');
  await page.getByRole('button', { name: 'Continue', exact: true }).click();
  await page.getByRole('button', { name: 'Manage Staff' }).click();
  await page.getByRole('button', { name: 'Edit' }).nth(1).click();
  await page.getByPlaceholder('Enter first name').click();
  await page.getByPlaceholder('Enter first name').fill('Felix');
  await page.getByPlaceholder('Enter last name').click();
  await page.getByPlaceholder('Enter last name').fill('Zhang');
  await page.getByRole('button', { name: 'Update' }).click();
  await expect(page.getByText('Staff updated successfully')).toBeVisible();
  await page.goto('http://localhost:3000/manageStaff');
  await expect(
    page.getByRole('heading', { name: 'Felix Zhang' })
  ).toBeVisible();
});
