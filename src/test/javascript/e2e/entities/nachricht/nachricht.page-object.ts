import { element, by, ElementFinder } from 'protractor';

export class NachrichtComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-nachricht div table .btn-danger'));
  title = element.all(by.css('jhi-nachricht div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class NachrichtUpdatePage {
  pageTitle = element(by.id('jhi-nachricht-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  anInput = element(by.id('field_an'));
  betreffInput = element(by.id('field_betreff'));
  textInput = element(by.id('field_text'));
  anhangInput = element(by.id('file_anhang'));
  statusSelect = element(by.id('field_status'));
  createdInput = element(by.id('field_created'));

  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAnInput(an: string): Promise<void> {
    await this.anInput.sendKeys(an);
  }

  async getAnInput(): Promise<string> {
    return await this.anInput.getAttribute('value');
  }

  async setBetreffInput(betreff: string): Promise<void> {
    await this.betreffInput.sendKeys(betreff);
  }

  async getBetreffInput(): Promise<string> {
    return await this.betreffInput.getAttribute('value');
  }

  async setTextInput(text: string): Promise<void> {
    await this.textInput.sendKeys(text);
  }

  async getTextInput(): Promise<string> {
    return await this.textInput.getAttribute('value');
  }

  async setAnhangInput(anhang: string): Promise<void> {
    await this.anhangInput.sendKeys(anhang);
  }

  async getAnhangInput(): Promise<string> {
    return await this.anhangInput.getAttribute('value');
  }

  async setStatusSelect(status: string): Promise<void> {
    await this.statusSelect.sendKeys(status);
  }

  async getStatusSelect(): Promise<string> {
    return await this.statusSelect.element(by.css('option:checked')).getText();
  }

  async statusSelectLastOption(): Promise<void> {
    await this.statusSelect.all(by.tagName('option')).last().click();
  }

  async setCreatedInput(created: string): Promise<void> {
    await this.createdInput.sendKeys(created);
  }

  async getCreatedInput(): Promise<string> {
    return await this.createdInput.getAttribute('value');
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class NachrichtDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-nachricht-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-nachricht'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
