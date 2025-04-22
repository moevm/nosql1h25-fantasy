import {
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Book } from '../data-access/book.service';
import { Series } from '../data-access/series.service';
import { Movie } from '../data-access/movie.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-details',
  imports: [NgIf],
  templateUrl: './details.component.html',
  styleUrl: './details.component.less',
  standalone: true,
})
export class DetailsComponent {
  @Input() object: Book | Series | Movie | null;
  @Output() back = new EventEmitter<void>();

  constructor(private route: ActivatedRoute) {
    this.object = null;
  }

  getBook() {
    return this.object?.type === 'BOOK'
      ? (this.object as Book)
      : null;
  }

  getSeries() {
    return this.object?.type === 'SERIES'
      ? (this.object as Series)
      : null;
  }

  getMovie() {
    return this.object?.type === 'FILM'
      ? (this.object as Movie)
      : null;
  }

  goBack() {
    this.back.emit();
  }
}
