import { element, by, ElementFinder } from 'protractor';

export class DokumentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-dokument div table .btn-danger'));
  title = element.all(by.css('jhi-dokument div h2#page-heading span')).first();
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

export class DokumentUpdatePage {
  pageTitle = element(by.id('jhi-dokument-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nummerInput = element(by.id('field_nummer'));
  dokumentSelect = element(by.id('field_dokument'));
  textInput = element(by.id('field_text'));
  dataInput = element(by.id('file_data'));
  createdInput = element(by.id('field_created'));

  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNummerInput(nummer: string): Promise<void> {
    await this.nummerInput.sendKeys(nummer);
  }

  async getNummerInput(): Promise<string> {
    return await this.nummerInput.getAttribute('value');
  }

  async setDokumentSelect(dokument: string): Promise<void> {
    await this.dokumentSelect.sendKeys(dokument);
  }

  async getDokumentSelect(): Promise<string> {
    return await this.dokumentSelect.element(by.css('option:checked')).getText();
  }

  async dokumentSelectLastOption(): Promise<void> {
    await this.dokumentSelect.all(by.tagName('option')).last().click();
  }

  async setTextInput(text: string): Promise<void> {
    await this.textInput.sendKeys(text);
  }

  async getTextInput(): Promise<string> {
    return await this.textInput.getAttribute('value');
  }

  async setDataInput(data: string): Promise<void> {
    await this.dataInput.sendKeys(data);
  }

  async getDataInput(): Promise<string> {
    return await this.dataInput.getAttribute('value');
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

export class DokumentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-dokument-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-dokument'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
