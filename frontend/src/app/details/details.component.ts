import {
  AfterViewInit,
  Component,
  computed,
  EventEmitter,
  inject,
  input,
  InputSignal,
  output,
  Output,
  OutputEmitterRef,
  signal,
} from '@angular/core';
import { Book } from '../data-access/book.service';
import { Series } from '../data-access/series.service';
import { Movie } from '../data-access/movie.service';
import { NgIf } from '@angular/common';
import { TuiButton, TuiDialog } from '@taiga-ui/core';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { TuiInputModule } from '@taiga-ui/legacy';
import { TuiAutoFocus } from '@taiga-ui/cdk';
import { HttpClient } from '@angular/common/http';
import { rootActions } from '../store/root-store/root.actions';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-details',
  imports: [
    NgIf,
    TuiDialog,
    ReactiveFormsModule,
    TuiInputModule,
    TuiAutoFocus,
    TuiButton,
  ],
  templateUrl: './details.component.html',
  styleUrl: './details.component.less',
  standalone: true,
})
export class DetailsComponent implements AfterViewInit {
  public inputObject: InputSignal<Book | Series | Movie | undefined> =
    input();
  @Output() back = new EventEmitter<void>();
  public updated: OutputEmitterRef<Book | Series | Movie> = output();

  protected open = false;
  private url = 'http://localhost:8080/catalog';
  private http = inject(HttpClient);
  private store = inject(Store);

  private localObject = signal<Book | Series | Movie | undefined>(
    undefined
  );

  protected object = computed(
    () => this.localObject() ?? this.inputObject()
  );

  protected editForm = new FormGroup({
    editTitle: new FormControl(''),
    editType: new FormControl(''),
    editDescription: new FormControl(''),
    editStartYear: new FormControl(''),
    editEndYear: new FormControl(''),
    editTags: new FormControl(''),
    editRating: new FormControl(''),
    editCountry: new FormControl(''),
    editQuantityPages: new FormControl(''),
    editDuration: new FormControl(''),
    editSeasons: new FormControl(''),
    editPersons: new FormArray([]),
  });

  ngAfterViewInit() {
    if (this.object()) {
      this.editForm.patchValue({
        editTitle: this.object()?.title ?? '',
        editType: this.object()?.type ?? '',
        editDescription: this.object()?.description ?? '',
        editStartYear: this.object()?.startYear.toString() ?? '',
        editEndYear: this.object()?.endYear.toString() ?? '',
        editTags: this.object()?.tags.join(', ') ?? '',
        editRating: this.object()?.rating.toString() ?? '',
        editCountry: this.object()?.country ?? '',
        editQuantityPages:
          this.getBook()?.quantityPages?.toString() ?? '',
        editDuration: this.getMovie()?.duration?.toString() ?? '',
        editSeasons: this.getSeries()?.seasons?.toString() ?? '',
      });

      this.populatePersons();
    }
  }

  private populatePersons(): void {
    const persons = this.object()?.persons || [];
    const personsFormArray = this.editForm.get(
      'editPersons'
    ) as FormArray;
    persons.forEach(person => {
      personsFormArray.push(
        new FormGroup({
          role: new FormControl(person.role),
          name: new FormControl(person.name),
        })
      );
    });
  }

  getBook() {
    return this.object()?.type === 'BOOK'
      ? (this.object() as Book)
      : null;
  }

  getSeries() {
    return this.object()?.type === 'SERIES'
      ? (this.object() as Series)
      : null;
  }

  getMovie() {
    return this.object()?.type === 'FILM'
      ? (this.object() as Movie)
      : null;
  }

  goBack() {
    this.back.emit();
  }

  protected get editPersons(): FormArray {
    return this.editForm.get('editPersons') as FormArray;
  }

  protected showDialog(): void {
    this.open = true;
  }

  protected submitEdit(): void {
    if (this.editForm.invalid) {
      console.warn('Форма заполнена некорректно!');
      return;
    }

    const personsArray = (
      this.editForm.get('editPersons') as FormArray
    ).value;

    const payload = {
      id: this.object()?.id,
      title: this.editForm.value.editTitle,
      type: this.editForm.value.editType,
      description: this.editForm.value.editDescription,
      startYear: Number(this.editForm.value.editStartYear),
      endYear: Number(this.editForm.value.editEndYear),
      tags: this.editForm.value.editTags
        ? this.editForm.value.editTags
            .split(',')
            .map((tag: string) =>
              tag.trim().toUpperCase().replace(/\s+/g, '_')
            )
        : '',
      rating: Number(this.editForm.value.editRating),
      country: this.editForm.value.editCountry,
      quantityPages: this.editForm.value.editQuantityPages
        ? Number(this.editForm.value.editQuantityPages)
        : undefined,
      duration: this.editForm.value.editDuration
        ? Number(this.editForm.value.editDuration)
        : undefined,
      seasons: this.editForm.value.editSeasons
        ? Number(this.editForm.value.editSeasons)
        : undefined,
      persons: personsArray,
    };

    this.http
      .put<
        Book | Series | Movie
      >(`${this.url}/${payload.id}`, payload)
      .subscribe({
        next: (updatedObject: Book | Series | Movie) => {
          this.localObject.set(updatedObject);
          this.store.dispatch(
            rootActions.objectUpdated({ updatedObject })
          );
          this.updated.emit(updatedObject);
        },
        error: (err: unknown) => {
          console.error('Ошибка при отправке формы:', err);
        },
      });
  }
}
